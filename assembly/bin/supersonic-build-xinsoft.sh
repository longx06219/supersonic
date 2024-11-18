#!/usr/bin/env bash

sbinDir=$(cd "$(dirname "$0")"; pwd)
chmod +x $sbinDir/supersonic-common.sh
source $sbinDir/supersonic-common.sh
cd $projectDir
MVN_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]')

cd $baseDir
service=$1
if [ -z "$service"  ]; then
  service=${STANDALONE_SERVICE}
fi

function buildJavaService {
  model_name=$1
  echo "starting building supersonic-${model_name} service"
#  mvn -f $projectDir clean package -DskipTests -Dspotless.skip=true
  mvn -f $projectDir clean install -DskipTests -Dspotless.skip=true -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true --settings ../settings.xml
  if [ $? -ne 0 ]; then
      echo "Failed to build backend Java modules."
      exit 1
  fi
  cp $projectDir/launchers/${model_name}/target/*.tar.gz ${buildDir}/
  echo "finished building supersonic-${model_name} service"
}

function buildWebapp {
  echo "starting building supersonic webapp"
  chmod +x $projectDir/webapp/start-fe-prod.sh
  cd $projectDir/webapp
  sh ./start-fe-prod.sh
  cp -fr  ./supersonic-webapp.tar.gz ${buildDir}/
  # check build result
  if [ $? -ne 0 ]; then
      echo "Failed to build frontend webapp."
      exit 1
  fi
  echo "finished building supersonic webapp"
}

function packageRelease {
  model_name=$1
  release_dir=supersonic-${model_name}-${MVN_VERSION}
  service_name=launchers-${model_name}-${MVN_VERSION}
  echo "starting packaging supersonic release"
  cd $buildDir
  [ -d "$release_dir" ] && rm -rf "$release_dir"
#  [ -f "$release_dir.zip" ] && rm -f "$release_dir.zip"
  [ -f "$release_dir.tar.gz" ] && rm -f "$release_dir.tar.gz"
  mkdir $release_dir
  # package webapp
  tar xvf supersonic-webapp.tar.gz
  mv supersonic-webapp webapp
  json='{"env": "''"}'
  echo $json > webapp/supersonic.config.json
  mv webapp $release_dir/
  # package java service
  tar xvf $service_name-bin.tar.gz
  mv $service_name/* $release_dir/
  # generate zip file
#  zip -r $release_dir.zip $release_dir
  tar -zcvf $release_dir.tar.gz $release_dir
  # delete intermediate files
  rm supersonic-webapp.tar.gz $service_name-bin.tar.gz
  rm -rf webapp $service_name $release_dir
  echo "finished packaging supersonic release"
}

#1. build backend services
if [ "$service" == "webapp" ]; then
  buildWebapp
elif [ "$service" == "backend" ]; then
  buildJavaService ${STANDALONE_SERVICE}
  packageRelease ${STANDALONE_SERVICE}
else
  buildJavaService $service
  buildWebapp
  packageRelease $service
fi