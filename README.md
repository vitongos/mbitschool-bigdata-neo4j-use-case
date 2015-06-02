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

### Instalar Java 8
Descargar [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/java8.sh
```
Ante el mensaje *Enter to keep the current selection[+], or type selection number:* seleccionar la opción del **jdk1.8.0_45**.

### Instalar Eclipse
Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/eclipse.sh
```

### Instalar Storm
Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/storm.sh
```

### Instalar Beanstalkd
Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/beanstalkd.sh
```

### Instalar Neo4j
Ejecutar el siguente script:
```bash
cd ~/use-case-src/
deploy/neo4j.sh
```




