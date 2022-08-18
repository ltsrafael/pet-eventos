import { LoginService } from './login.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';


const oAuthConfig: AuthConfig = {
  issuer: 'https://accounts.google.com',
  strictDiscoveryDocumentValidation: false,
  redirectUri: window.location.origin,
  clientId:'657287624814-1r3qplao0r58041gojvmee5qvhvpsprt.apps.googleusercontent.com',
  scope:'openid profile email'
}

@Injectable({
  providedIn: 'root'
})
export class GoogleApiService {

  constructor(private readonly oAuthService: OAuthService, private route: ActivatedRoute, private loginService: LoginService,  private router: Router) {}

  public async signIn() {
      this.oAuthService.configure(oAuthConfig);
      this.oAuthService.logoutUrl = "https://www.google.com/accounts/Logout";
      this.oAuthService.loadDiscoveryDocument().then( () => {
        this.oAuthService.tryLoginImplicitFlow().then( () => {
          if (!this.oAuthService.hasValidAccessToken()) {
            this.oAuthService.initLoginFlow();
            localStorage.setItem('access_token',this.oAuthService.getAccessToken())
          } else {
            let token = this.oAuthService.getAccessToken();
            let accessToken = this.route.snapshot.params['access_token'];
          }

        })
      });
  }

  googleAuthenticate(accessToken: string) {
    this.loginService
        .googleAuthenticate(accessToken)
        .subscribe( (response:any) => {
          let tokenStr = "Bearer " + response.body['token'];
          localStorage.setItem("token", tokenStr);
          if(response.body['userType'] == 'ADMIN') {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/event']);
          }
        }, error => {

        })
  }
}
