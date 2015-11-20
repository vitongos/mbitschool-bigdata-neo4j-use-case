Demos y fuentes del caso de grafos persistidos en Big Data
==========================================================

Instalación
-----------

A continuación se especifica el proceso de instalación de los diferentes sistemas necesarios para ejecutar las demos.

*Importante: ejecutar la máquina virtual con 8 GB de RAM*

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
Ante el mensaje *Enter to keep the current selection[+], or type selection number:* seleccionar la opción del **jdk1.8.0_65**.

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

### Instalar Neo4j
Ejecutar el siguiente script:
```bash
cd ~/use-case-src/
deploy/neo4j.sh
```

### Ejecutar los proyectos
Ejecutar el Eclipse desde una consola:
```bash
eclipse &
```

Importar al Eclipse los 5 proyectos dentro del directorio **src**.

Configurar el Build Path de cada proyecto para que apunte al JRE de Java 8: */usr/local/java/jdk1.8.0_65/*.

Compilar el cliente de beanstalkd sin ejecutar los tests:
- Clic derecho en **com.dinstone.beanstalkc**
- *Run as... > Maven build...*
- Como *Goals* indicar *clean install*
- Marcar el checkbox *Skip Tests*
- Run

A continuación, compilar los otros cuatro proyectos.

Ejecutar los proyectos en el siguiente orden:
- **socket**: habilita un socket para que escuche el storm y comience a enviar tuplas
- **beanstalk**: es el worker que consumirá los datos procesados, puede ejecutarse múltiples veces si se quiere que se consuman más rápido
- **spark**: levanta el servidor Web que presentará la simulación
- **storm**: ejecutando la topología se comienzan a consumir tuplas desencadenando todo el proceso

