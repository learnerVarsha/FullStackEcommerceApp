import { Injectable } from '@angular/core';

const TOKEN = 'ecom-token';
const USER = 'ecom-user';
@Injectable({
  providedIn: 'root'
})
export class UserStorageService {
  
  constructor() { }

  public saveToken(token: string): void{
    window.localStorage.removeItem(TOKEN);
    window.localStorage.setItem(TOKEN, token);
  }

  public saveUser(userId): void{
    window.localStorage.removeItem(USER);
    window.localStorage.setItem(USER, userId);
  }
}
