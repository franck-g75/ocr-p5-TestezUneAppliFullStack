import { HttpClientModule, HttpClient, HttpHandler } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';


import { SessionApiService } from '../../services/session-api.service';
import { BehaviorSubject, Observable, of } from 'rxjs';

class MySessionApiServiceMock {
  participate = jest.fn().mockReturnValue(of(null));
  unParticipate = jest.fn().mockReturnValue(of(null));
  delete =jest.fn().mockReturnValue(of(null));
}

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;
  let http: HttpClient;
  let mySessionApiServiceMock: MySessionApiServiceMock;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  // Spy sur window.history.back()
  const wbackSpy = jest.fn();

  beforeAll(() => {
    // On remplace la méthode réelle par le spy
    Object.defineProperty(window, 'history', {
      value: { back: wbackSpy },
      writable: true,
    });
    
  });

  afterEach(() => {
    // Réinitialiser le compteur du spy entre les tests
    wbackSpy.mockClear();
  });

  beforeEach(async () => {
    
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: SessionService, useValue: mockSessionService }, 
        { provide: SessionApiService, useClass: MySessionApiServiceMock}
      ]

    }).compileComponents();
   
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    http = TestBed.inject(HttpClient);

    //fixture.detectChanges();  // Applique le binding et rend le DOM disponible

    //valeur d'exemple :
    component.userId = "1";
    component.sessionId = "1";

    mySessionApiServiceMock = TestBed.inject(SessionApiService) as unknown as MySessionApiServiceMock;
    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call window.history.back() when back() is invoked', () => {
    component.back();                    // appel de la méthode du composant
    expect(wbackSpy).toHaveBeenCalled(); // vérification du spy
  });

  it('should call this.sessionApiService.participate() when component.participate() is invoked', () => {
               
    // 👉  Cast en `any` pour accéder à la méthode privée
    const privateSpy = jest.spyOn(component as any, 'fetchSession' as any).mockImplementation(() => {});
    component.participate();
    expect(mySessionApiServiceMock.participate).toHaveBeenCalledWith(
      component.sessionId,
      component.userId
    );
    //La souscription se déclenche immédiatement grâce à `of(null)`                         
    expect(privateSpy).toHaveBeenCalled();
  });

  it('should call this.sessionApiService.unParticipate() when component.unParticipate() is invoked', () => {
    
    // 👉  Cast en `any` pour accéder à la méthode privée
    const privateSpy = jest.spyOn(component as any, 'fetchSession' as any).mockImplementation(() => {});
    component.unParticipate();
    expect(mySessionApiServiceMock.unParticipate).toHaveBeenCalledWith(
      component.sessionId,
      component.userId
    );
    //La souscription se déclenche immédiatement grâce à `of(null)`                         
    expect(privateSpy).toHaveBeenCalled();

  });

  it('should call component.fetchSession() when component.ngOnInit() is invoked', () => {
    
    const fetchSpy = jest.spyOn(component as any, 'fetchSession').mockReturnThis();
    component.ngOnInit();                       // appel de la méthode du composant
    expect(fetchSpy).toHaveBeenCalledTimes(1);  // vérification du spy
    
  });

  it('should call sessionApiService.delete() when component.delete() is invoked', () => {
    
    component.delete();
    expect(mySessionApiServiceMock.delete).toHaveBeenCalledWith(component.sessionId);
    
  });

  

});

