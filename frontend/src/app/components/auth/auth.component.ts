import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginUser } from 'src/app/models/user/login-user';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { TokenService } from 'src/app/services/authentication/token.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  formLogin: FormGroup
  loginUser: LoginUser | undefined
  messageError: string | undefined

  constructor(private tokenService: TokenService, private formBuilder: FormBuilder, private authenticationService: AuthenticationService, private router: Router) {
    this.formLogin = this.formBuilder.group({
      username: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(5)]],
    })
  }

  ngOnInit(): void {
    if(this.tokenService.getToken()) {
      this.router.navigateByUrl("/app/welcome")
    }
  }

  onLogin() {
    this.loginUser = new LoginUser(this.formLogin.value.username, this.formLogin.value.password)
    this.authenticationService.login(this.loginUser).subscribe({
      next: (n) => {
        this.loginSuccessful(n)
      },
      error: (e) => {
        this.returnError(e)
      }
    })
  }

  loginSuccessful(response: any) {
    this.tokenService.setToken(response.token)
    this.tokenService.setUsername(response.username)
    this.router.navigateByUrl("/app/welcome")
  }

  returnError(err: any) {
    let returned_error = err.error.text
    if(returned_error == undefined) {
      returned_error = 'Incorrect user'
    }
    this.messageError = returned_error
  }

}
