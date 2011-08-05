#! /bin/sh
# This script takes care of most of the steps for setting up a working development environment
# The script assumes debian based linux in 64bit architecture. If you have a different system
#
# Note: this script hasn't been tested yet, it is recommended you run it line by line to check that it does what it says

echo "*********** Creating yeshira development environment"
YESHIRA_HOME=~/yeshira2
mkdir "$YESHIRA_HOME"
cd "$YESHIRA_HOME"

# * CouchDB and Sun JDK
echo "Installing CouchDB, Java and Git..."
sudo apt-get install couchdb sun-java6-jdk git
echo "*********** Downloading and Installing Eclipse..."
wget "http://mirrors.ustc.edu.cn/eclipse/technology/epp/downloads/release/indigo/R/eclipse-jee-indigo-linux-gtk-x86_64.tar.gz" -O - | tar -zx
echo "*********** Downloaing and installing Maven 3"
wget "http://apache.spd.co.il//maven/binaries/apache-maven-3.0.3-bin.tar.gz" -O - | tar -zx

echo "*********** Adding Maven and Java environment variable to startup script"
echo "export M3_HOME=$YESHIRA_HOME/apache-maven-3.0.3" | sudo tee -a /etc/profile
echo "export JAVA_HOME=/usr/lib/jvm/java-6-sun/jre" | sudo tee -a /etc/profile
echo "export JRE_HOME=/usr/lib/jvm/java-6-sun/jre" | sudo tee -a /etc/profile
echo "export JDK_HOME=/usr/lib/jvm/java-6-sun" | sudo tee -a /etc/profile
echo
echo "*********** Creating workspace"
mkdir "$YESHIRA_HOME/workspace"
cd "$YESHIRA_HOME/workspace"
echo "*********** Cloning project from GitHub"
git clone "git://github.com/iddo/Yeshira.git"
echo
echo "*********** Development environment installed in $YESHIRA_HOME"
echo	
echo "*********** To run a local jetty server use this command"	
echo $YESHIRA_HOME/workspace/Yeshira/yeshira/web/start-server.sh"
echo
# TODO: make this work
#echo "*********** Installing Eclipse plugins - Maven, Git"
#$YESHIRA_HOME/eclipse/eclipse -application org.eclipse.equinox.p2.director -repository "http://download.eclipse.org/egit/updates" -installIU "org.eclipse.egit,org.eclipse.jgit"
echo "*********** To finish configuration do the following steps:
echo "1. Install 'Maven Integration for Eclipse' and 'EGit' Eclipse plugins through the Eclipse Market Place
echo "2. Setup Maven plugin to support project settings (Window->Preferences->Maven->User Settings, and choose user settings file settings.xml located in $YESHIRA_HOME/workspace/Yeshira/yeshira/web), and to use the maven installation at $YESHIRA_HOME/apache-maven-3.0.3 
echo "3. Import yeshira web project from $YESHIRA_HOME/workspace/Yeshira (File->Import->Existing project into workspace)"
echo "4. Right click on project and select Team->Share Project->Git->Use or Create repository in parent folder of project
echo "5. Right click on project and select 
echo
echo "*********** Starting Eclipse..."

$YESHIRA_HOME/eclipse/eclipse -data $YESHIRA_HOME/workspace