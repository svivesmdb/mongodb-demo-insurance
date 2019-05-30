
if command -v python3 &>/dev/null; then
    echo "✅  Python 3 is installed!"
    
else
    echo "🛑  Python 3 is not installed, please install the official python3, following this guide: https://evansdianga.com/install-pip-osx/"
    exit
fi

if command -V pip3 &>/dev/null; then
    echo "✅  PIP 3 is installed!"
else
    echo "🛑  PIP 3 is not installed, please install it following this guide: https://evansdianga.com/install-pip-osx/ "
    exit
fi

if command -v docker &>/dev/null; then
    echo "✅  Docker is installed!"
else
    echo "🛑  Docker desktop is not installed, download it from: https://docs.docker.com/docker-for-mac/install/"
    exit
fi


#
#  curl -fsSL https://get.docker.com -o get-docker.sh
# sudo sh get-docker.sh
#


echo "🐍  Installing python requirements"

sudo pip3 install -r ./mongodb-demo-insurance/data/data_generation/requirements.txt &>make.log

#4. To use cx_Oracle with Oracle Instant Client zip files:
echo "🗜  Unzipping oracle driver..."
sudo mkdir -p /opt/oracle
sudo unzip ./oracle_instantclient/instantclient-basic-macos.x64-18.1.0.0.0.zip -d /opt/oracle
sudo mkdir ~/lib
sudo ln -s /opt/oracle/instantclient_18_1/libclntsh.dylib ~/lib
# REFER TO: https://cx-oracle.readthedocs.io/en/latest/installation.html#install-oracle-instant-client


echo "🗜  Unzipping IOT driving data files.."
unzip -o ./mongodb-demo-insurance/data/data_generation/output.zip -d ./mongodb-demo-insurance/data/data_generation/ &>make.log

cd shell_scripts

# Docker setup to create the containers and run them. Then you should be able to start 
echo "🐋  Starting docker setup"
sh dockersetup.sh &>make.log


echo "🥂  Finished!"
