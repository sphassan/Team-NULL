#!/usr/bin/python

import smtplib

fromaddr = 'test@gmail.com'
toaddrs = 'example@example.com'

msg = """Subject: This is a subject? 


    testy test
"""

print "message length is " + repr(len(msg))

#### Server settings ####
### AWS console > SMTP settings 
smtp_server     = 'email-smtp.us-east-1.amazonaws.com'
smtp_username   = '***************'
smtp_password   = '****************************'
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






