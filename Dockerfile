FROM alpine:3.18

# add general dependencies
RUN apk add git make

# install yt-dlp
RUN apk add python3
RUN git clone https://github.com/yt-dlp/yt-dlp /var/www/yt-dlp
RUN echo "#!/usr/bin/env sh" > /usr/bin/yt-dlp
RUN echo 'exec "${PYTHON:-python3}" -Werror -Xdev "/var/www/yt-dlp/yt_dlp/__main__.py" "$@"' >> /usr/bin/yt-dlp
RUN chmod +x /usr/bin/yt-dlp

# install clojure
RUN apk add clojure leiningen

# run the application
WORKDIR /var/www/music-player
COPY ./ /var/www/music-player

EXPOSE 8080

CMD ["lein", "run"]
