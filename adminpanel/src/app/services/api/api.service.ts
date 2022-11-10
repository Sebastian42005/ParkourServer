import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  private readonly baseUrl: string = 'http://localhost:8080/api'

  public get<T>(url: string): Observable<T> {
    return this.http.get<T>(this.baseUrl + url);
  }

  public post<T>(url: string, body: any): Observable<T> {
    return this.http.post<T>(this.baseUrl + url, body);
  }

  public delete<T>(url: string): Observable<T> {
    return this.http.delete<T>(this.baseUrl + url);
  }

  public put<T>(url: string, body: any): Observable<T> {
    return this.http.put<T>(this.baseUrl + url, body);
  }

}
