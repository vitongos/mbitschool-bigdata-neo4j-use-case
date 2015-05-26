Demos y fuentes del caso de grafos persistidos en Big Data
==========================================================

Instalación
-----------

A continuación se especifica el proceso de instalación de los diferentes sistemas necesarios para ejecutar las demos.

### Instalar el repositorio clonándolo de Github:
Ejecutar el siguiente script:
```bash
sudo yum -y install git
cd
git clone https://github.com/vitongos/mbitschool-bigdata-neo4j-use-case.git use-case-src
chmod +x use-case-src/deploy/*.sh
```

### Instalar Neo4j
Ejecutar el siguente script:
```bash
cd ~/use-case-src/
deploy/neo4j.sh
```

### Instalar Eclipse
Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/eclipse.sh
```

### Instalar Java 8
Descargar [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/java8.sh
```


