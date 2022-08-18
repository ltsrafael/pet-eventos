import { GoogleApiService } from './google-api.service';
import { User } from './../model/User';
import { Component, ElementRef, NgZone, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { LoginService } from './login.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user!: User;
  error: any;
  tokenStr: any;
  loginForm: FormGroup;
  forgetPasswordForm: FormGroup;
  processing = false;
  forgotPassword = false;
  passwordReseted = false;
  login: any;
  title = 'loginGoogle';
  auth2: any;
  googleError: any;


  @ViewChild('loginRef', {static: false }) loginElement!: ElementRef;

  loginForm2!: FormGroup;
  isLoggedin?: boolean;

  constructor(private loginService: LoginService, private router: Router, private formBuilder: FormBuilder, private _snackBar: MatSnackBar, private googleApiService : GoogleApiService, private route: ActivatedRoute, private zone: NgZone) {}

  ngOnInit(): void {
    this.loginService.isAuthenticated();
    this.user = new User();
    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required,Validators.email]],
      password: [null, Validators.required]
    })
    this.forgetPasswordForm = this.formBuilder.group({
      email: [null, [Validators.required,Validators.email]]
    })
    this.login = true;
  }

  callLoginButton() {
    this.auth2.attachClickHandler(this.loginElement.nativeElement, {},
      (googleAuthUser:any) => {
        this.googleAuthenticate(googleAuthUser.Cc.access_token);
      }, (error:any) => {
      });
  }

  loginWithGoogle(): void {
    this.zone.runOutsideAngular(() => {
      this.googleAuthSDK();
    });
  }

  googleAuthSDK() {
    (<any>window)['googleSDKLoaded'] = () => {
      (<any>window)['gapi'].load('auth2', () => {
        this.auth2 = (<any>window)['gapi'].auth2.init({
          issuer: 'https://accounts.google.com',
          strictDiscoveryDocumentValidation: false,
          redirectUri: window.location.origin,
          clientId:'657287624814-r6u5er6ucbuov555ma58b7rb5qjmin9p.apps.googleusercontent.com',
          scope:'openid profile email',
          plugin_name: "chat"
        });
        this.callLoginButton();
      });
    }

    (function(d, s, id){
      var js, fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) {return;}
      js = d.createElement('script');
      js.id = id;
      js.src = "https://apis.google.com/js/platform.js?onload=googleSDKLoaded";
      fjs?.parentNode?.insertBefore(js, fjs);
    }(document, 'script', 'google-jssdk'));
  }

  googleAuthenticate(accessToken: string) {
    this.loginService
        .googleAuthenticate(accessToken)
        .subscribe( (response:any) => {
          if(response.statusCodeValue != 400) {
            let tokenStr = "Bearer " + response.body['token'];
            localStorage.setItem("token", tokenStr);
            if(response.body['userType'] == 'ADMIN') {
              this.zone.run(() => {
                this.router.navigate(['/admin']);
              });
            } else {
              this.zone.run(() => {
                this.router.navigate(['/event']);
              });
            }
          }
          if(response.statusCodeValue == 400) {
            this.googleError = response.body;
            this.zone.run(() => {
              this._snackBar.open(response.body, 'Ok', {
                duration: 3000
              });
            });
          }
        }, error => {

        })
  }

  openSnackBar() {
    this._snackBar.open(this.error, 'Ok', {
      duration: 3000
    });
  }

  authenticate() {
    this.processing = true;
    this.loginService
        .authenticate(this.user.email,this.user.password)
        .subscribe( (response:any) => {
          this.processing = false;
          this.tokenStr = "Bearer " + response.body['token'];
          localStorage.setItem("token", this.tokenStr);
          if(response.body['userType'] == 'ADMIN') {
            this.zone.run(() => {
              this.router.navigate(['/admin']);
            });
          } else {
            this.zone.run(() => {
              this.router.navigate(['/event']);
            });
          }
        }, error => {
          this.processing = false;
          this.error = error;
        })
  }

  resetPassword() {
    this.processing = true;
    this.loginService
        .resetPassword(this.user.email)
        .subscribe( (response:any) => {
          this.processing = false;
          this.forgotPassword = false;
          this.passwordReseted = true;
        }, error => {
          this.processing = false;
          this.error = error.error.message;
          this.openSnackBar();
        })
  }

  home() {
    this.error = "";
    this.forgotPassword = false;
    this.passwordReseted = false;
    this.processing = false;
    this.login = true;
  }

  forgetPassword() {
    this.login = false;
    this.forgotPassword = true;
  }

}
