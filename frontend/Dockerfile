FROM node:18.17.1
WORKDIR /app
COPY package*.json ./
RUN npm install
RUN npm install -g serve
COPY . .
RUN NODE_ENV=production npm run build
ENTRYPOINT ["serve", "-s", "dist"]