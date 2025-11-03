# Portfolio Backend - Analytics with Redis

Skeleton Spring Boot project integrated in the same backend to track visits and store the last 20 visits (IP + User-Agent + path).

## What's included
- POST /api/track  -> track visits
- GET  /api/stats  -> total visits, unique visitors, last 20 visits
- Redis integration (StringRedisTemplate)
- Docker Compose with Redis and the API service
- Swagger UI (springdoc) for API exploration

## Quick start
1. Build the project:

```
mvn clean package
```

2. Start Redis + API with Docker Compose:

```
docker-compose up --build
```

3. Start your React frontend and make sure it calls POST /api/track at startup.

4. Open Swagger UI: http://localhost:8080/swagger-ui.html

## Frontend snippet

Include this in your React app (e.g. in App.js):

```js
useEffect(() => {
  fetch('http://localhost:8080/api/track', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ path: window.location.pathname, referrer: document.referrer })
  }).catch(err => console.warn('track error', err));
}, []);
```

## Notes
- Adjust CORS origins if your frontend runs on a different domain.
- Consider GDPR/privacy implications of storing IPs; anonymize or hash if necessary.
