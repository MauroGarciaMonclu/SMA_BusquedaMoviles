prepare:
	cp jsoup-1.11.3.jar ../../lib/jsoup-1.11.3.jar
	cp makefile ../../makefile
	@echo "Muevase al directorio principal jade/ y ejecute los comandos 'export CLASSPATH=$CLASSPATH:./lib/jsoup-1.11.3.jar' y 'make compile'"
compile:
	javac -d classes/ src/SMA_BusquedaMoviles/dominio/*.java
test:
	java jade.Boot -gui -agents "C:SMA_BusquedaMoviles.dominio.ScrapCarrefour;ECI:SMA_BusquedaMoviles.dominio.ScrapElCorteIngles;W:SMA_BusquedaMoviles.dominio.ScrapWorten;MM:SMA_BusquedaMoviles.dominio.ScrapMediaMark;Buscador:SMA_BusquedaMoviles.dominio.Buscador;Cliente:SMA_BusquedaMoviles.dominio.Cliente(samsung galaxy s10)"
run:
	java jade.Boot -gui -agents "C:SMA_BusquedaMoviles.dominio.ScrapCarrefour;ECI:SMA_BusquedaMoviles.dominio.ScrapElCorteIngles;W:SMA_BusquedaMoviles.dominio.ScrapWorten;MM:SMA_BusquedaMoviles.dominio.ScrapMediaMark;Buscador:SMA_BusquedaMoviles.dominio.Buscador"
