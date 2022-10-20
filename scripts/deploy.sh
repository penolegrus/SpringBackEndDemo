
cd ..
./gradlew clean build

scp -rp -i /Users/o.pendrak/Downloads/SpringBootUbuntuStudy-KGju0M3S.pem /Users/o.pendrak/Documents/ThreadQaBackEnd/untitled/build/libs/untitled-1.0-SNAPSHOT.jar ubuntu@85.192.34.140:/home/ubuntu

ssh -i /Users/o.pendrak/Downloads/SpringBootUbuntuStudy-KGju0M3S.pem ubuntu@85.192.34.140 << EOF
pgrep java | xargs kill -9
java -jar untitled-1.0-SNAPSHOT.jar

EOF
