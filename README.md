LithiumSSO
==========

It is a sample application to interface with Lithium Community Standard Cookie SSO

> you need NOT to know grail/groovy to run this application.
> You may run it with any windows server, through the instruction below is for a Unix user.

# Install Grails
1. cd . 
  * change to home directory
2. wget http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-2.3.6.zip
  * latest Grails release URL can be found with http://www.grails.org/download
3. unzip grails-2.3.6.zip
  * "grails-2.3.6/" will be added to your home directory
  * rm grails-2.3.6.zip
4. vi +$ .bash_profile
  * add "export GRAILS_HOME=~/grails-2.3.6"
  * add "export PATH=$PATH:$GRAILS_HOME/bin"
5. . bash_profile
  * just to reload what we set in previous step
  
# Upgrade Grails (only IF you download the latest grails)
1. from command, run 
  * grails upgrade
  
# Configuration (Customer Profile Settings)
1. cd <path>/grails-app/services/lithium/apac/apacsso
2. vi +$ LithiumSSOService.groovy
  * update the setting string which will pass to Lithium Community
  * reqRemoteAddr = request.getHeader("HTTP_X_FORWARDED_FOR" supports to get the real IP address if the application is behind a firewall.
3. save the file

# Configuration (Email and Host)
1. cd <path>/grails-app/conf/
2. vi +$ Config.groovy
  * update grails.mail.default.from, grails.mail.username, grails.mail.password
    - we need this configuration to confirm the registration.
  * grails.plugin.springsecurity.ui.register.postRegisterUrl
    - this is the URL after user verified the registration.
3. save the file

# Configuration (temprary)
> this is for passing a referer to the registration link, if user didn't click ->register from community directly, but use ->login->register
1. cd <path>/grails-app/view/login/
2. vi +$ auth.gsp
  * look for http://apac.sage.lithium.com, update to your community url
3. save the file

# Run it
1. cd <path>
2. grails run-app
  * for better performance, you can use "grails -Ddisable.auto.recompile=true  run-app", but after you change any groovy files, you may need to run "grails compile" first.
3. you will see something like "Server running. Browse to http://localhost:8090/"

# Configure your Community
1. Community -> Admin->SSO, shortcut: https://apac.stage.lithium.com/t5/bizapps/bizappspage/tab/community%3Aadmin%3Asystem%3Asso
2. update the SSO hostname in hyperlinks below
  * SSO registration page: http://haidong-gu2.vm.lithium.com:8090/SSORegister
  * SSO login page: http://haidong-gu2.vm.lithium.com:8090/SSOLogin
  * SSO logoff page: http://haidong-gu2.vm.lithium.com:8090/SSOLogout

# FAQ
1. the default setting is to use in-memory database (development), to set a persistent database (production), you can use "grails prod run-app", instead of "grails run-app".
2. you may have to make sure your community and SSO server are in the same domain. To run it locally, just "hack" your windows localhost file or Unix hosts file.




