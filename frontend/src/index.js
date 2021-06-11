import axios from "axios";

const $container = document.getElementsByClassName("container");

const modelId = getParameterByName("id")
window.onload = getModel(modelId)


function getModel(modelId) {
    console.log(modelId)

    axios.get("http://3.35.24.12/test/api/" + modelId)
        .then(res => {
            console.log(res)
            $container.innerHTML = res
        })
        .catch(e => console.log(e))
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
