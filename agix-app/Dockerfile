
# COMPILE DIST
FROM node:16-alpine as build-step
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build-prod

# EXECUTE DIST
FROM nginx:1.17.1-alpine
EXPOSE 80
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build-step /app/dist /usr/share/nginx/html
VOLUME /usr/share/nginx/html
