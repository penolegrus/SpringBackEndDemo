ssh -i /Users/o.pendrak/Downloads/SpringBootUbuntuStudy-KGju0M3S.pem ubuntu@85.192.34.140 << EOF
pgrep node | xargs kill -9
cd /var/www/threadqa.ru/trainDemo
live-server --port=8081

EOF