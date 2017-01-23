
echo "################################# Taks Queue API #################################"
echo "Build application? Yes(y) No(n)?"
read COMPILAR
if [ "$COMPILAR" == "y" ];then
   cd source
   docker run -it --rm -v $PWD:/usr/src/taskqueue-api -w /usr/src/taskqueue-api maven:3 mvn clean install
   cd ..
   mkdir -p $PWD/dist
   cp $PWD/source/target/taskqueue-api.war $PWD/dist
   cd dist
   docker build --tag=wildfly-app .
   docker run -it -p 8080:8080  wildfly-app
else
    if [ "$COMPILAR" == "n" ]; then
        cd dist
        docker build --tag=wildfly-app .
        docker run -it -p 8080:8080  wildfly-app
    else
        echo "Invalid option!"
    fi
fi

