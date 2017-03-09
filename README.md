<h1>Project X</h1>

<p> Project X - сервис, позволяющий бесперебойно обмениваться цитатами между двумя датацентрами.</p>
<h2> Запуск и настройка проекта для добавления новых цитат</h2>
<p>
	Для корректной работы приложения должны быть установлены, настроены и запущены:
	<ul>
		<li> MongoDB </li>
		<li> Apache Kafka</li>
	</ul>

	<p> <h3>Настройка MongoDB</h3>
		Для настройки MongoDB необходимо внести изменения в файл application.yml в секцию spring.data.mongodb. Все документы указанной коллекции базы данных должны содержать поля text и author.
	<p/>

	<p> <h3>Настройка Apache Kafka</h3>
		Для работы  приложения необходимо, чтобы на сервере Kafka были созданы следующие топики: quote-local, quote-replica, quote-reserve (названия можно изменить в application.yml). Для достижения полной синхронизации и отказоустойчивости при передаче цитат между датацентрами, приложение использует два kafka-consumer и один kafka-producer. Продюссер нужен для записи в топики quote-local и quote-replica, находящиеся на сервере kafka местного датацентра. Один консьюмер нужен для чтения из топика quote-local местного датацентра, другой для чтения топика quote-replica удалённого датацентра. Настройки можно найти в application.yml.
	</p>

	<p> <h3>Отказоустойчивость</h3>
		Для достижения отказоустойчивости в случае неполадки одного датацентра используется переадресация на другой датацентр. Для настройки переадресации необходимо внести изменения в файл application.yml в поле server.remote-host.
	<p/>

</p>
<p>
	<h3> Сборка и запуск проекта</h3>
	В качестве сборщика проекта используется Maven. Для сборки и запуска проекта необходимо перейти в папку Server. Выполнить: <code lang="bash">mvn clean package</code> для сборки проекта. Для запуска: <code lang="bash">java -jar target/project-x-spring-server-1.0-SNAPSHOT.jar</code>

</p>
