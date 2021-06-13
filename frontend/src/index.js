import axios from "axios";
import "./main.css";
// import testData from "./testdata/test";

const modelId = getParameterByName("id")
window.onload = getModel(modelId)

function getModel(modelId) {
    console.log(modelId)

    axios.get("http://3.35.24.12/test/api/" + modelId)
        .then(res => {
            console.log(res)
            showDetail(res)
        })
        .catch(e => console.log(e))
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}


function showDetail(data) {
    console.log(data)

    const $header = document.querySelector(".card-header");
    const $cultureName = document.querySelector(".card-title");
    const $img = document.querySelector("img");
    const $explanation = document.querySelector(".card-text");
    const $date = document.querySelector(".date");
    const $location = document.querySelector(".location");

    $header.append(data.culture)
    $cultureName.append(data.cultureName)
    $img.setAttribute("src", data.imageUrl)
    $explanation.append(data.explanation)
    $location.append(`위치: ${data.location}`)

    const startDate = new Date(data.startDate)
    const endDate =new Date(data.endDate)
    const dateString = `${startDate.getMonth()}월 ${startDate.getDay()}일 ~ ${endDate.getMonth()}월 ${endDate.getDay()}일`

    $date.append(dateString);


}

