Demos y fuentes del caso de grafos persistidos en Big Data
==========================================================

Instalación
-----------

A continuación se especifica el proceso de instalación de los diferentes sistemas necesarios para ejecutar las demos en Centos 7.4.
Ejecutar los scripts bajo usuario root.

*Importante: ejecutar la máquina virtual con mínimo 4 GB de RAM, 8 GB recomendados*

### Instalar el repositorio clonándolo de Github:
Ejecutar el siguiente script bajo:
```bash
yum -y install git
cd
git clone https://github.com/vitongos/mbitschool-bigdata-neo4j-use-case.git use-case-src
chmod +x use-case-src/deploy/*.sh
chown centos:centos /opt -R
```

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

### Instalar Memcached
Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/memcached.sh
```

Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/neo4j.sh
```

### Compilar los proyectos
Ejecutar el Eclipse desde una consola:
```bash
eclipse &
```

Importar al Eclipse los 5 proyectos dentro del directorio **src**.

Configurar el Build Path de cada proyecto para que apunte al JDK de Java 8 (por ejemplo al de OpenJDK).

Compilar el cliente de beanstalkd sin ejecutar los tests:
- Clic derecho en **com.dinstone.beanstalkc**
- *Run as... > Maven build...*
- Marcar el checkbox *Skip Tests*
- Como *Goals* indicar *clean install package*
- Run

A continuación, compilar los proyectos **beanstalk**, **spark**, **storm**:
- *Run as... > Maven build...*
- Como *Goals* indicar *clean install package*
- Run


### Ejecutar los proyectos desde Eclipse

Ejecutar los proyectos como Java Application en el siguiente orden:
- **socket**: habilita un socket para que escuche el storm y comience a enviar tuplas (clase *MainClass*)
- **beanstalk**: es el worker que consumirá los datos procesados, puede ejecutarse múltiples veces si se quiere que se consuman más rápido (clase *Worker*)
- **spark**: levanta el servidor Web que presentará la simulación (clase *AppController*)
- **storm**: ejecutando la topología se comienzan a consumir tuplas desencadenando todo el proceso (clase *MyTopology*)


### Ejecutar los proyectos desde consola de comandos

Copiar el jar del cliente de Beanstalkd al directorio de librerías de Storm y ejecutar los proyectos desde 4 consolas de comandos:
```bash
cd ~/use-case-src/
cp src/com.dinston.beanstalkc/target/beanstalk-client-2.1.0-jar-with-dependencies.jar  /opt/storm/lib/

java -cp src/socket/bin socket.MainClass

java -jar src/worker/target/beanstalk-0.0.1-SNAPSHOT-jar-with-dependencies.jar

java -jar src/spark/target/spark-0.0.1-SNAPSHOT-jar-with-dependencies.jar

storm jar src/storm/target/storm-0.0.1-SNAPSHOT.jar storm.MyTopology
```


