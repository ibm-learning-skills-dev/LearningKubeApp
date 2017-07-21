from node:6

ADD . /StoreWebApp

WORKDIR /StoreWebApp

RUN npm install
RUN ln -s /StoreWebApp/node_modules/bower/bin/bower /usr/local/bin/bower
RUN bower install --allow-root

CMD [ "npm", "start" ]
