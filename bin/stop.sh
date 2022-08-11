#!/bin/sh 
kill -9 `cat < bin/pwd.pid`
rm bin/pwd.pid