var getBaseUrl = () => {
    let baseUrl = window.location.href.slice(0, -window.location.search.length);
    if (baseUrl[baseUrl.length - 1] != '/')
        baseUrl = baseUrl + '/';

    return baseUrl;
};

var axiosConfig = {
    baseURL: this.getBaseUrl(),
};
var axiosInstance = axios.create(axiosConfig);

function signUpMethod(form)
{
    csrf = document.getElementById("csrf-id");
    csrf_name = csrf.getAttribute('name');
    csrf_value = csrf.value;
    const data = new FormData(document.forms.namedItem('signUpInfo'));
    let body = {};
    secret = {};
    user = {};
    hobbies = data.getAll('hobbies');
    entries =  Object.fromEntries(data.entries());
    Object.entries(entries).forEach(([key, val]) => {
        if (key.indexOf('secret.') != -1)
            secret[key.substr(key.indexOf('.')+1, key.length-1)] = val;
        if (key.indexOf('user.') != -1)
            user[key.substr(key.indexOf('.')+1, key.length-1)] = val;
    });
    body['secret'] = secret;
    body['user'] = user;
    body['hobbies'] = hobbies;

    try {
        axiosInstance({
            method: 'POST',
            url: "/api/v1/auth/signup",
            headers: {
                'Content-Type' : 'application/json',
                csrf_name: csrf_value
            },
            data: body
        }).then( response => {
            if (response.status == 200)
                window.location.href = response.data.data.url;
        }).catch(function (error){
            alert(error.response.data.msg);
        });
    }catch (errors) {
        console.error(errors);
    }
}

function addFriend(userId)
{
    csrf = document.getElementById("csrf-id");
    csrf_name = csrf.getAttribute('name');
    csrf_value = csrf.value;
    try {
        axiosInstance({
            method: 'post',
            url: "/api/v1/user/friends/add/" + userId,
            headers: {
                csrf_name: csrf_value,
                'Content-Type' : 'application/json'
            },
            data: {}
        }).then((response) => response.data.msg != '' ? alert(response.data.msg) : "return")
        .catch(function (error){
            alert(error.response.data.msg);
        })
    }catch (errors) {
        console.error(errors);
    }
}

function findUsers(name)
{
    csrf = document.getElementById("csrf-id");
    csrf_name = csrf.getAttribute('name');
    csrf_value = csrf.value;
    let div = document.getElementById("foundUsers-id");
    div.replaceChildren();
    try {
        axiosInstance({
            method: 'get',
            url: "/users/search/" + name,
            headers: {
                csrf_name: csrf_value,
                'Content-Type' : 'application/json'
            },
            data: {}
        }).then((response) => {
            if (response.data){
                let div = document.getElementById("foundUsers-id");
                if (div) div.insertAdjacentHTML('beforeend', response.data);
            }
        }).catch(function (error){
                alert(error.response.data.msg);
            })
    }catch (errors) {
        console.error(errors);
    }
}

