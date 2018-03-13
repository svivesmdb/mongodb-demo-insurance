## Development mode

Run the application.
Notes:
* Assumption is that the first command is executed from the directory `<repo-root>/mainframe_offloading/legacy-insurer-portal`
* `npm install` is only required for the first time or when dependencies change

```
host$ docker run -it --rm -v $(pwd):/home/app -p 3000:3000  node:9.5 /bin/bash
container$ cd /home/app
container$ npm install
container$ npm start
```

Open a browser pointing at http://localhost:3000/