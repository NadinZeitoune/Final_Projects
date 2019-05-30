window.onload = function(){
    
}

function showMenu(){
    let index = id_menu.className.indexOf(" d-block");
    console.log(index);
    if(index < 0){
        id_menu.className += " d-block";
        console.log(id_menu.className);
    }else{
        id_menu.className = id_menu.className.replace(" d-block", "");
        console.log(id_menu.className);
    }
}