var li = document.getElementsByTagName('li');
for(item in li){
    li[item].onclick = function(event){
        this.style = 'background-color:red';
    }
}