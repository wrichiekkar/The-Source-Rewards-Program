// listen for auth status changes
auth.onAuthStateChanged(user => {
  if (user) {
    console.log('user logged in: ', user);
    document.getElementById("non-authenticated").style.display = "none";
    document.getElementById("authenticated").style.display = "initial";
  } else {
    console.log('user logged out');
    document.getElementById("authenticated").style.display = "none";
    document.getElementById("non-authenticated").style.display = "initial";
  }
})

// signup
const signupForm = document.querySelector('#signup-form');
signupForm.addEventListener('submit', (e) => {
  e.preventDefault();
  
  // get user info
  const email = signupForm['signup-email'].value;
  const password = signupForm['signup-password'].value;
  // sign up the user
  auth.createUserWithEmailAndPassword(email, password).then(cred => {
    // close the signup modal & reset form
    const modal = document.querySelector('#modal-signup');
    M.Modal.getInstance(modal).close();
    signupForm.reset();
    window.alert("You have now signed up and are now logged in");
  });
  // Error Message
  firebase.auth().createUserWithEmailAndPassword(email,password).catch(function(error) {
    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;
    window.alert(errorMessage)
  });
});

// logout
const logout = document.querySelector('#logout');
logout.addEventListener('click', (e) => {
  e.preventDefault();
  auth.signOut();
  window.alert("You are now logged out");
});

// login
const loginForm = document.querySelector('#login-form');
loginForm.addEventListener('submit', (e) => {
  e.preventDefault();
  
  // get user info
  const email = loginForm['login-email'].value;
  const password = loginForm['login-password'].value;
  // Error Message
  firebase.auth().signInWithEmailAndPassword(email,password).catch(function(error) {
    // Handle Errors here.
    var errorCode = error.code;
    var errorMessage = error.message;
    window.alert(errorMessage)
    // ...
  });

  // log the user in
  auth.signInWithEmailAndPassword(email, password).then((cred) => {
    window.alert("You are now logged in");
    // close the signup modal & reset form
    const modal = document.querySelector('#modal-login');
    M.Modal.getInstance(modal).close();
    loginForm.reset();
    
  });
});
