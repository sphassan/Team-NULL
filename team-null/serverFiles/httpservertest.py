#!/usr/bin/env python
"""
Very simple HTTP server in python.

Usage::
    ./dummy-web-server.py [<port>]

Send a GET request::
    curl http://localhost

Send a HEAD request::
    curl -I http://localhost

Send a POST request::
    curl -d "foo=bar&bin=baz" http://localhost

"""
from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import SocketServer

from validate_email import validate_email

import uuid
import re
import MySQLdb
import os
import sys

db = MySQLdb.connect(host="localhost",user="*******",passwd="********",db="users")

cur = db.cursor()


#sys.stderr
log = open("http.log",'w')
sanName = re.compile(r'^[A-Za-z]+$')

class S(BaseHTTPRequestHandler):
    def _set_headers(s):
        s.send_response(200)
        s.send_header('Content-type', 'text/html')
        s.end_headers()

    def do_GET(s):
        s._set_headers()
        s.wfile.write("<html></html>")

    def do_HEAD(s):
        s._set_headers()
        
    def do_POST(s):
        # Doesn't do anything with posted data
        length = int(s.headers.getheader('content-length'))
        data = s.rfile.read(length) 
        temp = data.split("(")
        fuct = temp[0];
        yelo = temp[1];
        data2 = yelo[:-1].split(",");
        uid = "";
        first = "";
        last  = "";
        email = "";
        print str(data2)
        if fuct == "register":
            email = data2[0];
            first = data2[1];
            last  = data2[2];
            
        elif fuct == "stats":
            uid   = data2[0];

        elif fuct == "reregister":
            uid   = data2[0];
            email = data2[1];
            first = data2[2];
            last  = data2[3];

        is_valid = validate_email(email,check_mx=True)
        is_first = sanName.match(first)
        is_last  = sanName.match(last)
        if is_first is None:
            print "invaild first name"+first+" "
        if is_last is None:
            print "invaild last name "+last+" "
        if not is_valid:
            print "invaid email "+ email +" "
        
        if (not is_valid) or (is_first is None) or (is_last is None):
            s.send_response(666)
        elif fuct == "register":
            s.send_response(200)
            uid = str(uuid.uuid4())
            os.system("./emails.py "+email+" "+first+" welcomeMsg.txt")  

            s.send_header('Content-type','text/html')
            s.end_headers()
            s.wfile.write("<html> "+uid+" </html>")
        else:
            s._set_headers()
def register(first, last, email):
    print first+" "+last+" "+email
    
def stats(uid,stats):
    print ""

def reregister(uid, first, last, email):
    print ""

def run(server_class=HTTPServer, handler_class=S, port=80):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print 'Starting httpd...'
    httpd.serve_forever()

if __name__ == "__main__":
    from sys import argv

    if len(argv) == 2:
        run(port=int(8080))
    else:
        run()
