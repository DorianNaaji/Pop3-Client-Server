echo -e "\e[34m\e[1m[ cleaning folder ]\e[0m"

rm server.jks
rm server.cer
rm client.jks

echo -e "\e[34m\e[1m[ create server keystore ]\e[0m"
keytool -genkey -keypass password \
                -storepass password \
                -keystore server.jks

echo -e "\e[34m\e[1m[ export public certificat form server keystore ]\e[0m"
keytool -export -storepass password \
                -file server.cer \
                -keystore server.jks

echo -e "\e[34m\e[1m[ create client keystore with public certificat]\e[0m"
keytool -import -v -trustcacerts \
                -file server.cer \
                -keypass password \
                -storepass password \
                -keystore client.jks