var grabar = document.getElementById("grabar")
grabar.addEventListener("click", GRABAR)
var detener = document.getElementById("detener")
detener.addEventListener("click", DETENER)

var chunks = [];
var mediaRecorder;

navigator.mediaDevices.getUserMedia({
  audio: false, video: true
}).then(function (x) {
  /* usar el flujo de datos */
  mediaRecorder = new MediaRecorder(x);

  var camara = document.getElementById("camara")
  camara.srcObject = x
  camara.onloadedmetadata = function (e) {
    camara.play()
  };

  mediaRecorder.onstop = function (e) {

    var clipName = prompt('Enter a name for your sound clip');

    var clipContainer = document.createElement('article');
    var clipLabel = document.createElement('p');
    var audio = document.createElement('video');
    audio.width="150"
    var deleteButton = document.createElement('button');
    // generar una liga
    var a = document.createElement('a')
    var texto = document.createTextNode("descarga")
    a.appendChild(texto)

    clipContainer.classList.add('clip');
    audio.setAttribute('controls', '');
    deleteButton.innerHTML = "Delete";
    clipLabel.innerHTML = clipName;

    var soundClips = document.getElementById("xxx")
    clipContainer.appendChild(audio);
    clipContainer.appendChild(clipLabel);
    clipContainer.appendChild(deleteButton);
    clipContainer.appendChild(a)
    soundClips.appendChild(clipContainer);

    audio.controls = true;
    var blob = new Blob(chunks, { 'type': 'video/webm; codecs=vp8' });
    chunks = [];
    var audioURL = URL.createObjectURL(blob);
    audio.src = audioURL;
    console.log("recorder stopped");
    a.href = audioURL
    a.download = "video.mp4"

    deleteButton.onclick = function(e) {
      evtTgt = e.target;
      evtTgt.parentNode.parentNode.removeChild(evtTgt.parentNode);
    }
  }

  mediaRecorder.ondataavailable = function(e) {
    chunks.push(e.data);
    enviar(e.data)
  }

  }).catch(function (err) {

  });

function GRABAR(params) {
  mediaRecorder.start();
}

function DETENER(params) {
  mediaRecorder.stop();
}

function enviar(stream) {
  var formData = new FormData();
  let pagina = document.getElementById("pagina");

  formData.append("pagina", pagina.getAttribute("value"));
  formData.append("videoGrabado", stream);
  let url = document.getElementById("url-service");
  axios.post(url.getAttribute("value"), formData, {
    headers : {
      "Content-Type" : "multipart/form-data"
    }
  }).then((response) => {
    console.log(response);
  });
}