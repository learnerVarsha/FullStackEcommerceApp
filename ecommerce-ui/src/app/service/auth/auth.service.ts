import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';
import { AuthResponseDTO } from '../../models/auth-response.dto';
import { MatSnackBar } from '@angular/material/snack-bar';

const BASIC_URL = "http://localhost:8080/";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    private http: HttpClient,
    private userStorageService: UserStorageService,
    private snackbar: MatSnackBar
  ) { }

  register(signUpRequest: any): Observable<any> {
    return this.http.post(BASIC_URL + "signup", signUpRequest);
  }

  login(username: string, password: string): Observable<boolean> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    const body = { username, password };
  
    return this.http.post<AuthResponseDTO>(BASIC_URL + 'authenticate', body, { headers, observe: 'response' }).pipe(
      map((res) => {
        const authHeader = res.headers.get('Authorization');  // Check header
        if (!authHeader) {
          return false;  // Handle missing token case
        }
  
        const token = authHeader.startsWith('Bearer ') ? authHeader.substring(7) : authHeader;
        const responseBody = res.body;
  
        if (token && responseBody?.userId) {
          this.userStorageService.saveToken(token);
          this.userStorageService.saveUser(responseBody.userId);  // Save only userId
          return true;
        }
        return false;
      }),
      catchError((error) => {
        this.handleError(error);
        return of(false);  // Handle errors gracefully
      })
    );
  }

  private handleError(error: any): void {
    console.error('Login error', error); // Log error for debugging
  
    if (error.status === 0) {
      // Network issues, server down, or CORS issue
      this.snackbar.open('Please check your internet connection or try again later.', 'OK', { duration: 5000 });
    } else if (error.status === 401) {
      // Unauthorized access - invalid credentials
      this.snackbar.open('Invalid username or password. Please try again.', 'OK', { duration: 5000 });
    } else if (error.status === 500) {
      // Server error
      this.snackbar.open('Server error, please try again later.', 'OK', { duration: 5000 });
    } else {
      // Other errors
      this.snackbar.open('Something went wrong, please try again.', 'OK', { duration: 5000 });
    }
  }
  
}
