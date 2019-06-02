const EMAIL = "nadinz811@gmail.com";

window.onload = function(){
    
}

function sendMail(){
    let name;
    let mail;
    let sb;
    let content;

    name = id_name.value;
    mail = id_email.value;
    sb = id_subject.value;
    content = `${id_content.value} \n \n ${name} \n ${mail}`;
    
    //window.open(`https://mail.google.com/mail/?view=cm&fs=1&to=${EMAIL}&su=${sb}&body=${content}`);
}