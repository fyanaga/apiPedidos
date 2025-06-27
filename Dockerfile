#Instalando uma imagem do JDK 21
FROM openjdk:21

#Criando uma pasta para onde o docker irá copiar os arquivos do projeto
WORKDIR /app

#Copiando os arquivos do projeto para dentro desta pasta
COPY . /app

#Comando para realizar o DEPLOY do projeto
RUN ./mvnw -B clean package -DskipTests

#Porta em que o projeto será executado
EXPOSE 8081

#Comando para executar o projeto depois do DEPLOY
CMD ["java", "-jar", "target/apiPedidos-0.0.1-SNAPSHOT.jar"]