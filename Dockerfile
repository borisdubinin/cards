FROM tomcat:11

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/cards.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080