import axios from "axios";

const $container = document.getElementsByClassName("container");
const $button = document.getElementsByTagName("button");

window.onload = getModel(1)

function getModel(modelId) {
    console.log(modelId)

    axios.get("http://3.35.24.12/test/api/" + modelId)
        .then(res => {
            console.log(res)
            $container.innerHTML = res
        })
        .catch(e => console.log(e))
}