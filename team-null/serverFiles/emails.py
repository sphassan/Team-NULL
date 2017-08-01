#!/usr/bin/python

#
#
#./email cx@gmail.com brandon welcomeMsg.txt
#
#
#

import smtplib
import sys


fromaddr = 'glance.no.reply@gmail.com'
toaddrs = str(sys.argv[1]);
name = str(sys.argv[2]);

f = open(str(sys.argv[3]),'r');


msg = str(f.readline()) + "\nHello "+ name +","+ str(f.read())

print "message length is " + repr(len(msg))

#### Server settings ####
### AWS console > SMTP settings 
smtp_server     = 'email-smtp.us-east-1.amazonaws.com'
smtp_username   = 'AKIAIRMHME2L6XOVFIJQ'
smtp_password   = 'Apb54MSN6T9sG0l8rPgqpT4AKZ2TTCyh3Cs7gvbRFOzG'
smtp_port       = '587'
smtp_do_tls     = True

#### create server object ####
server  = smtplib.SMTP(
host    = smtp_server,
port    = smtp_port,
timeout = 10
)

server.set_debuglevel(10)
server.starttls()
server.ehlo()
server.login(smtp_username,smtp_password)
server.sendmail(fromaddr, toaddrs, msg)

print server.quit()






