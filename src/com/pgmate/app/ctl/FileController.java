package com.pgmate.app.ctl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.pgmate.app.dao.CPDAO;
import com.pgmate.app.dao.FileDAO;
import com.pgmate.app.model.ajax.CPRequest;
import com.pgmate.app.model.ajax.CPResponse;
import com.pgmate.app.util.CPRUtil;
import com.pgmate.app.util.CPUtil;
import com.pgmate.app.util.FileMeta;
import com.pgmate.app.util.SessionUtil;
import com.pgmate.lib.util.gson.GsonUtil;
import com.pgmate.lib.util.io.FileIO;
import com.pgmate.lib.util.io.FileUtil;
import com.pgmate.lib.util.lang.CommonUtil;
import com.pgmate.lib.util.map.SharedMap;

/**
 * @author Administrator
 *
 */
@Controller
public class FileController {

	private static Logger logger = LoggerFactory.getLogger( com.pgmate.app.ctl.FileController.class );
	
    // 파일 업로드 ( jquery-fileupload )
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> upload(MultipartHttpServletRequest request, @RequestParam("memberId") String memberId, 
			@RequestParam("grade") String grade, @RequestParam("regId") String regId, HttpServletResponse response) {
		
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
	    FileMeta fileMeta = null;
	    
		logger.debug("memberId : {}",memberId);
		logger.debug("grade : {}",grade);
		
		Iterator<String> itr = request.getFileNames();
		MultipartFile mpf = null;
		while (itr.hasNext()) {
			// get next MultipartFile
			mpf = request.getFile(itr.next());
			
			if (files.size() >= 10)
				files.pop();
			
			//  create new fileMeta
			fileMeta = new FileMeta();
			fileMeta.setFileName(mpf.getOriginalFilename());
			fileMeta.setFileSize(mpf.getSize() / 1024 + " Kb");
			fileMeta.setFileType(mpf.getContentType());
			
			String[] copyFile = makeDirectory(request, mpf, memberId);
			fileMeta.setFileUrl(copyFile[0]);
			
			try {
				fileMeta.setBytes(mpf.getBytes());
				logger.debug("Dest URL : {}", copyFile[0]);
				logger.debug("Dest Files : {}", copyFile[1]);
				// copy file to local disk (make sure the path "e.g.
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream(copyFile[1]));
				
				CPDAO cpDAO = new CPDAO();
				CPRequest nCPRequest = new CPRequest();
				nCPRequest.setData("grade", grade);
				nCPRequest.setData("memberId", memberId);
				nCPRequest.setData("folderNm", fileMeta.getFileUrl());
				nCPRequest.setData("fileNm", fileMeta.getFileName());
				nCPRequest.setData("fileSize", fileMeta.getFileSize());
				
				if(cpDAO.insert("PG_FILE", regId, nCPRequest.data)){
					logger.info("file upload DB Success "+fileMeta.getFileName());
				}else{
					logger.info("file upload DB Fail "+fileMeta.getFileName());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("file upload error"+e.getMessage());
				e.printStackTrace();
			}
			files.add(fileMeta);
			logger.info("upload file : {}",copyFile[1]);
		}
		return files;
	}
	//파일 다운로드
	@RequestMapping(value = "/uploadFile/get/{value}", method = RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response, @PathVariable String value) {
		try {
			SharedMap<String, Object> fileMap = new FileDAO().getByIdx(value).getRow(0);
			String path = fileMap.getString("folderNm");
			String requestUrl = request.getRequestURL().toString().toLowerCase();
			String agent = request.getHeader("USER-AGENT");
			byte[] originFile = FileIO.getBytes(path);

			response.reset();
			// 브라우저별 처리 필요
			// Chrome
			String filename = fileMap.getString("fileNm");
			
			if (agent.contains("MSIE") || agent.contains("Trident")) {
				filename = URLEncoder.encode(filename,"UTF-8").replaceAll("\\+", "%20");
			    response.setHeader("Content-Disposition", "attachment;filename=" + filename + ";");
			} else {
				filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
			    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			}
			
			
			if (agent.indexOf("MSIE 6.0") > -1 || agent.indexOf("MSIE 5.5") > -1) {
				response.setContentType("application/octet-stream;charset=utf-8");
				response.setHeader("Content-Transfer-ENCODING", "binary");
			} else {
				response.setContentType("application/octet-stream;charset=utf-8");
			}
			if (!requestUrl.startsWith("https")) {
				response.setHeader("Pragma", "no-cache");
			}
			response.setHeader("Cache-Control", "private");
			response.setHeader("Expires", "0");
			response.setContentLength(originFile.length);

			logger.info("FILE PATH : {}", path);
			FileCopyUtils.copy(originFile, response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 파일 사용안함 설정
	@RequestMapping(value = "/file/inactive/{idx}", method = RequestMethod.GET)
	public void inactiveList(HttpServletRequest request, HttpServletResponse response, @PathVariable String idx) {
		
		CPDAO cpDAO = new CPDAO();
		CPRequest cpRequest = new CPRequest();
		cpRequest.setData("idx", idx, "eq", "", true);
		cpRequest.setData("state", "폐기");
		
		cpDAO.update("PG_FILE", cpRequest.data);
	}
	/**
	 * 추가 작업은 Authentication Header 를 설정해서 이게 없으면 보내지 않는다.
	 * 아래는 GET, POST 등 사용가능하며 POST 전달 시 parameter =name 에 전체 경로를 전달해야 한다.
	 */
	@RequestMapping(value = "/file/get/**")
	public void get(HttpServletRequest request,HttpServletResponse response){
		String fileName = "";
	
		if(request.getMethod().equals("GET")){
			fileName = request.getRequestURI().replaceAll("/file/get/", "");
		}else{
			fileName = CommonUtil.toString(request.getParameter("name"));
		}
		logger.info("download : {}",CPUtil.getUploadDir()+fileName);
		File file  = new File(CPUtil.getUploadDir()+fileName);
		
		if(!file.exists()){
			String json = GsonUtil.toJson(
					new CPRUtil(new CPRequest(CPUtil.CP_TYPE_DONWLOAD))
					.resultNOT_FOUND("the file you are looking for does not exist", fileName)
					.cpResponse(),true,"");
			
			 OutputStream outputStream = null;
			 try{
				 outputStream = response.getOutputStream();
				 outputStream.write(json.getBytes(Charset.forName("UTF-8")));
				 outputStream.close(); 
			 }catch(Exception e){
				 logger.info("filedownload error"+e.getMessage());
			 }
			 return;
		}else{
			String mimeType= URLConnection.guessContentTypeFromName(file.getName());
	        if(mimeType==null){
	           logger.info("mimetype is not detectable, will take default");
	           mimeType = "application/octet-stream";
	        }
	        
	        String requestUrl = request.getRequestURL().toString().toLowerCase();
	        String agent=request.getHeader("USER-AGENT");
	        response.reset();
	      
	        
	        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() +"\""));
	        if (agent.indexOf("MSIE 6.0") > -1 || agent.indexOf("MSIE 5.5") > -1) {
	        	response.setContentType("application/octet-stream;charset=utf-8");
	        	response.setHeader("Content-Transfer-ENCODING", "binary");
	        }else{
	        	  response.setContentType(mimeType);
	        }
	        if(!requestUrl.startsWith("https")){
				response.setHeader("Pragma", "no-cache");
			}
        	response.setHeader("Cache-Control", "private");
			response.setHeader("Expires", "0");
	        
	       /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
	        //response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
	         
	        response.setContentLength((int)file.length());
	        try{
	        	InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	        	FileCopyUtils.copy(inputStream, response.getOutputStream());
	        }catch(Exception e){
	        	logger.info("filedownload error"+e.getMessage());
	        }          
		}
	}
	/*
	@RequestMapping(value = "/file/uploa", method = RequestMethod.POST)
	public @ResponseBody CPResponse upload2(@RequestParam("file") MultipartFile file) {
		
		CPUtil.setUploadDirectory();
		String path = CPUtil.CP_UPLOAD_DIR+CommonUtil.getCurrentDate("yyyyMMdd");
		
		logger.info("Upload Files");
		logger.info("Name        : {}",file.getName());
		logger.info("O_Name      : {}",file.getOriginalFilename());
		logger.info("Size        : {}",file.getSize());
		logger.info("ContentsType: {}",file.getContentType());
		logger.info("Location    : {}",path+File.separator+file.getOriginalFilename());
		
		String message = "success";
		
		if(!file.isEmpty()){
			try{
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(path,file.getOriginalFilename())));
                stream.write(bytes);
                stream.close();	
			}catch(Exception e){
				message = e.getMessage();
			}
			
		}else{
			message = "empty File : "+file.getOriginalFilename();
		}
		logger.info("upload	  : {}",message);
		if(message.equals("success")){
			return new CPRUtil(new CPRequest(CPUtil.CP_TYPE_UPLOAD))
		        		.resultOK()
		        		.cpResponse();
		}else{
			return new CPRUtil(new CPRequest(CPUtil.CP_TYPE_UPLOAD))
	        		.resultNOK(message)
	        		.cpResponse();
		}
		
	 
	}*/
	
	@RequestMapping(value = "/file/multiUpload", method = RequestMethod.POST)
	public @ResponseBody CPResponse multiUpload(@RequestParam("file") MultipartFile[] files) {
		
		CPUtil.setUploadDirectory();
		String path = CPUtil.getUploadDir()+CommonUtil.getCurrentDate("yyyyMMdd");
		String message = "success";			
		
		if (files != null && files.length >0) {
    		for(int i =0 ;i< files.length; i++){

    			String name = files[i].getOriginalFilename();
    			logger.info("Upload Files {}",i);
    			logger.info("O_Name      : {}",files[i].getOriginalFilename());
    			logger.info("Size        : {}",files[i].getSize());
    			logger.info("ContentsType: {}",files[i].getContentType());
    			logger.info("Location    : {}",path+File.separator+files[i].getOriginalFilename());
				
				
				try{
					byte[] bytes = files[i].getBytes();
					BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(new File(path,name)));
	                stream.write(bytes);
	                stream.close();
	                message += "uploaded ["+i+","+name+"]";
				}catch(Exception e){
					message += "upload fail ["+i+","+name+" "+e.getMessage()+"]";
				}
				logger.info("upload	  : {}",message);
				
			
    		}
		}
		
		if(message.startsWith("success")){
			return new CPRUtil(new CPRequest(CPUtil.CP_TYPE_UPLOAD))
		        		.resultOK(message)
		        		.cpResponse();
		}else{
			return new CPRUtil(new CPRequest(CPUtil.CP_TYPE_UPLOAD))
	        		.resultNOK(message)
	        		.cpResponse();
		}
		
	 
	}
	
	
	
	
	private static String[] makeDirectory(MultipartHttpServletRequest request,MultipartFile mpf,String memberId){
		FileUtil fileUtil = new FileUtil();
		
		 String path = CPUtil.getUploadDir();
		
		if(!fileUtil.existDirectory(path)){
			fileUtil.makeDirectory(path);
		}
		
		if(!fileUtil.existDirectory(path+File.separator+memberId)){
			fileUtil.makeDirectory(path+File.separator+memberId);
		}
		
		String destDirectory = path+File.separator+memberId+File.separator+CommonUtil.getCurrentDate("yyyyMMdd");
		if(!fileUtil.existDirectory(destDirectory)){
			fileUtil.makeDirectory(destDirectory);
		}
		
		String extName = mpf.getOriginalFilename().substring(mpf.getOriginalFilename().lastIndexOf(".")+1);
		String fileName = CommonUtil.getCurrentDate("yyyyMMddHHmmssSSS")+"."+extName;
		logger.debug("fileName : "+fileName);
		return new String[]{CPUtil.getUploadDir()+"/"+memberId+"/"+CommonUtil.getCurrentDate("yyyyMMdd")+"/"+fileName,destDirectory+File.separator+fileName};
	}
}

