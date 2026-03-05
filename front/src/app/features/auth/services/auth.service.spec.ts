

import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import {HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';

describe('AuthService', () => {

  let service: AuthService;
  let httpMock: HttpTestingController;
  let pathService = 'api/auth'; //pathService private

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers:[AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    // Vérifie qu’aucune requête HTTP inattendue n’est restée en suspens
    httpMock.verify(); //not a fonction ?
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call httpClient.post on register registerRequest', () => {
    
    const registerRequest: RegisterRequest = {
      email: 'fg@mail.fr',
      firstName: 'franck',
      lastName: 'guindeuil',
      password: 'pwd'
    };

    service.register(registerRequest).subscribe();              // Souscription au résultat (on ne s’intéresse pas à la valeur ici)
    const req = httpMock.expectOne(pathService + '/register');  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('POST');
    
    req.flush(null);                                            // Simuler une réponse serveur (vide ou objet)

  });

  it('should call httpClient.post on login loginRequest', () => {
    
    const loginRequest: LoginRequest = {
      email: 'fg@mail.fr',
      password: 'pwd'
    };

    service.login(loginRequest).subscribe();                 // Souscription au résultat (on ne s’intéresse pas à la valeur ici)
    const req = httpMock.expectOne(pathService + '/login');  // Expectation sur la requête HTTP générée
    expect(req.request.method).toBe('POST');
    
    req.flush(null);                                         // Simuler une réponse serveur (vide ou objet)

  });

});
