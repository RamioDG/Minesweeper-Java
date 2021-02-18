FROM openjdk:8

RUN mkdir /minesweeper

WORKDIR /minesweeper

RUN apt-get update
RUN apt-get install curl
RUN apt-get install -y libxrender1 libxtst6 libxi6

RUN curl -L -H "Accept: application/vnd.github.v3+json" -H "Authorization: {token}" https://api.github.com/repos/RamioDG/Minesweeper-java/actions/artifacts/41793512/zip --output minesweeper.zip

RUN unzip minesweeper.zip

CMD ["java", "-jar", "Minesweeper-1.0.jar"]
