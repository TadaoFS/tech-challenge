# Tech Challange

---

## Tecnologias

* JDK 21
* Spring Web MVC
* PostgreSQL

## Requisitos

* JDK 21
* Alguma IDE (Intellij ou Visual Code)
* Maven
* Docker
* Docker Compose

## Construir e executar

### Construir artefato
Executar o comando abaixo para executar testes e gerar o jar executável do Spring boot:
```
 mvn clean package -DskipTests
```

### Executar Docker Compose
Rodar o docker-compose para subir toda a infra e o serviço:
```
docker-compose up --build -d
```

### Mostrar logs do serviço
Rodar o comando abaixo para visualizar os logs do serviço:
```
docker logs -f techchallange
```

### Destruir o serviço
Toda vez que atualizar o código, é necessário destruir o serviço e subir novamente. Para isso, execute o comando abaixo:
```
docker-compose down
```