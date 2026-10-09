// Local OpenAI HTTP transport fixture. This is NOT R8/provider validation.
const http = require('node:http');
let requests = 0;
http.createServer((req,res) => {
  if(req.url==='/fixture-metrics') {
    res.writeHead(200,{'content-type':'application/json'});
    res.end(JSON.stringify({requests,provider:'explicit local transport double'})); return;
  }
  if(req.method!=='POST'||req.url!=='/v1/chat/completions') {res.writeHead(404);res.end();return;}
  req.resume(); req.on('end',()=>{
    requests++;
    res.writeHead(200,{'content-type':'application/json'});
    res.end(JSON.stringify({id:'local-fixture',object:'chat.completion',choices:[{index:0,
      message:{role:'assistant',content:JSON.stringify({finalScore:80,feedback:'Explicit local transport fixture. No provider called.'})},finish_reason:'stop'}]}));
  });
}).listen(5010,'127.0.0.1',()=>console.log('Local transport double listening at 127.0.0.1:5010'));
