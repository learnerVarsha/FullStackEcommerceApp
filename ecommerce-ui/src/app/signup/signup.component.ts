import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth/auth.service';

@Component({
  selector: 'app-signup',
  standalone: false,
  
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {

  signUpForm!: FormGroup;
  hidePassword= true;

  constructor(private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private authService: AuthService,
    private router: Router
  ){}

  ngOnInit(): void{
    this.signUpForm = this.fb.group({
      name: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      confirmPassword: [null, [Validators.required]]
    })
  }

  togglePasswordVisibility(){
    this.hidePassword = !this.hidePassword;
  }

  onSubmit(): void{
    const password = this.signUpForm.get('password')?.value;
    const confirmPassword = this.signUpForm.get('confirmPassword')?.value;
    
    if(password != confirmPassword){
      this.snackBar.open('Passwords do not match.', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
      return;
    }

    this.authService.register(this.signUpForm.value).subscribe(
      (response) => {
        this.snackBar.open('Sign Up successful', 'Close', {duration: 5000});
        this.router.navigateByUrl("/login");
      },
      (error) => {
        this.snackBar.open('Sign Up failed. Please try again', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
      }
    )
  }




}
