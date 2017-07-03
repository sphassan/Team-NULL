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

class S(BaseHTTPRequestHandler):
    def _set_headers(s):
        s.send_response(200)
        s.send_header('Content-type', 'text/html')
        s.end_headers()

    def do_GET(s):
       #s._set_headers()
        s.wfile.write("<html><body><h1>hi!</h1></body></html>")

    def do_HEAD(s):
        s._set_headers()
        
    def do_POST(s):
        # Doesn't do anything with posted data
        length = int(s.headers.getheader('content-length'))
        data = s.rfile.read(length) 
        data2 = data.split(",")
        print data2
        s._set_headers()
        s.wfile.write("<html><body><h1>POST!</h1></body></html><p>{}<p>".format(data2))
        
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

