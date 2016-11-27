# ATS

#Inspiration

Do you know the frustration when a device in your office has broken and you would 
have to send an email about it to someone and you just don't have the time or don't 
know who to send the mail to. Or the frustration as a maintenance professional when you 
just get an email that a coffee machine in this huge building is broken, and you have to 
send 3 emails to figure out where it actually is. ATS, Automated Ticketing System to the rescue.

#What it does

ATS automatically collects data from sensors and devices and when 
it detects something is broken or needs attention, it automatically creates a ticket 
and assigns it to the maintenance person closest to the problem with the expertise to handle the problem 
according to rules made by your company. Maintenance professionals have their own mobile app where 
they see their current tasks, their locations and priorities.

The maintenance professional have several options. Once they receive the tickets, 
they can accept and start working. They can raise an issue saying they have a problem 
fixing the issue or assigned task. Once, they are done, they can mark the issue resolved for now.

It also provides a dashboard for managerial or monitoring purposes with the list of personnel and 
the list of tasks assigned for each personnel. We have a plan to add many more features to both the 
android application and the dashboard. For now, a prototype of its working is near to complete.

#How we built it

We have a PHP backend with MySQL database that keeps track of the tickets, 
persons and rules in the company. It also receives the data from the devices and 
locations of the personnel with the Tieto API and uses that data along with the rules 
crafted by your company to automatically assign the tickets. We also built an android 
application for maintenance personnel, where they can see their tasks to do, specific information, 
location and error codes from the machine that is broken. They can also report a problem regarding 
the broken machine using the app. The foreman can control the flow of tickets manually, if needed, 
using the web dashboard. We also constructed a simple Arduino-based sensor and simulated the data 
generation and posting it to the backend.
