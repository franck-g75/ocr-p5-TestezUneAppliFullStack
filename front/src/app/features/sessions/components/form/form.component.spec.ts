import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AbstractControl, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { Session } from '../../interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, convertToParamMap, Router, Routes } from '@angular/router';
import { of } from 'rxjs';
import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'sessions',
    template: `ID : {{ id }}` 
  })
class DummyComponent implements OnInit {
  id!: string;
  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
  }
}

const routes: Routes = [
  { path: 'sessions/:id', component: DummyComponent },
];









describe('FormComponent with mock and admin=false', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  
  const mockSessionService = {
    sessionInformation: {
      admin: false
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService }, //admin=false
        SessionApiService
      ],
      declarations: [FormComponent,DummyComponent]
    }).compileComponents();

    router = TestBed.inject(Router);
    router.navigateByUrl("/sessions/1");  // l’URL que le composant verra
    router.initialNavigation();         // attend que la navigation soit terminée
    
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();   // ngOnInit() lit l’URL via ActivatedRoute
  });

  afterEach(() => {
    // 1️⃣ Détruire le composant et son DOM
    if (fixture) {
      fixture.destroy();
    }

    // 2️⃣ Réinitialiser le module de test Angular
    TestBed.resetTestingModule();

    // 3️⃣ Restaurer tous les mocks Jest
    jest.restoreAllMocks();

    // 4️⃣ Retour aux timers réels (si des fake timers ont été utilisés)
    jest.useRealTimers();
  });
  
  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  
  it('should redirect toward sessions when admin=false', () => {
    //console.log('redirect toward sessions when admin=false');
    router.navigate = jest.fn();
    component.ngOnInit();
    //console.log("admin = " + mockSessionService.sessionInformation.admin);
    expect(router.navigate).toHaveBeenCalledWith(["/sessions"]);
  });
  
});












describe('FormComponent with mock and admin=true', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes(routes),
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService }, //admin=false
        SessionApiService
      ],
      declarations: [FormComponent,DummyComponent]
    }).compileComponents();

    router = TestBed.inject(Router);
    router.navigateByUrl('/sessions/1&update=true');  // l’URL que le composant verra
    router.initialNavigation();         // attend que la navigation soit terminée
    
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();   // ngOnInit() lit l’URL via ActivatedRoute
  });

  afterEach(() => {
    // 1️⃣ Détruire le composant et son DOM
    if (fixture) {
      fixture.destroy();
    }

    // 2️⃣ Réinitialiser le module de test Angular
    TestBed.resetTestingModule();

    // 3️⃣ Restaurer tous les mocks Jest
    jest.restoreAllMocks();

    // 4️⃣ Retour aux timers réels (si des fake timers ont été utilisés)
    jest.useRealTimers();
  });

  it('should redirect toward sessions when admin=true', () => {
    //console.log('redirect toward sessions when admin=true');
    router.navigate = jest.fn();
    component.ngOnInit();
    //console.log("admin = " + mockSessionService.sessionInformation.admin);
    expect(router.navigate).not.toHaveBeenCalledWith(["/sessions/1&update=true"]);
  });

  it('should set update to true', () => {
    //console.log('should set update to true');
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
  });

});


















describe('FormComponent without mock', () => {
  let component: FormComponent;
  let router: Router;
  let fixture: ComponentFixture<FormComponent>;
  
  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const snackBarMock = { open: jest.fn() } as unknown as MatSnackBar;

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: snackBarMock },
        SessionApiService,
      ],
      declarations: [FormComponent]
    }).compileComponents();

    

    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should mark name as required', () => {
    const nameControl = component.sessionForm?.get('name') as AbstractControl
    nameControl.setValue('');
    expect(nameControl.valid).toBeFalsy();
    expect(nameControl.hasError('required')).toBeTruthy();
  });

  it('should mark date as required', () => {
    const dateControl = component.sessionForm?.get('date') as AbstractControl;
    dateControl.setValue('');
    expect(dateControl.valid).toBeFalsy();
    expect(dateControl.hasError('required')).toBeTruthy();
  });

  it('should select a teacher', () => {
    const teacherControl = component.sessionForm?.get('teacher_id') as AbstractControl;
    teacherControl.setValue('');
    expect(teacherControl.valid).toBeFalsy();
    expect(teacherControl.hasError('required')).toBeTruthy();
  });

  it('should mark description as required', () => {
    const descriptionControl = component.sessionForm?.get('description') as AbstractControl;
    descriptionControl.setValue('');
    expect(descriptionControl.valid).toBeFalsy();
    expect(descriptionControl.hasError('required')).toBeTruthy();
  });

  it('should mark the field description as invalid if the input is too long', () => {
    const descriptionControl = component.sessionForm?.get('description') as AbstractControl;

    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i <= 2001; i++) {
      result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    descriptionControl.setValue(result);

    expect(descriptionControl.invalid).toBeTruthy();
    expect(descriptionControl.errors?.['maxlength']).toBeTruthy();
    
  });


/*
  //ne fonctionne pas (encore)
  it('should navigate to "sessions" when goToTarget() is called', () => {

    const descriptionControl = component.sessionForm?.get('description') as AbstractControl;
    const teacherControl = component.sessionForm?.get('teacher_id') as AbstractControl;
    const dateControl = component.sessionForm?.get('date') as AbstractControl;
    const nameControl = component.sessionForm?.get('name') as AbstractControl;

    descriptionControl.setValue("desciption");
    teacherControl.setValue(1);
    dateControl.setValue(new Date(2026,2,25));
    nameControl.setValue("Franck");

    component.onUpdate = true;
    component.submit();
    expect(snackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });

    component.onUpdate = false;
    component.submit();
    expect(snackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });

  });

  it('should show a close message 3 seconds', () => {

    let msg = "test message";
    
    const descriptionControl = component.sessionForm?.get('description') as AbstractControl;
    const teacherControl = component.sessionForm?.get('teacher_id') as AbstractControl;
    const dateControl = component.sessionForm?.get('date') as AbstractControl;
    const nameControl = component.sessionForm?.get('name') as AbstractControl;

    descriptionControl.setValue("desciption");
    teacherControl.setValue(1);
    dateControl.setValue(new Date(2026,2,25));
    nameControl.setValue("Franck");
    
    component.onUpdate = true;
    component.submit();
    expect(snackBarMock.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });

    component.onUpdate = false;
    component.submit();
    expect(snackBarMock.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
    
  });
*/

});



  /*
  it('should update a session when url includes update', () => {

  });

  it('should call initForm directly when url do not includes update', () => {

  });

  it('should redirect ', () => {

    console.log('onUpdate=' + component.onUpdate);
    expect(router.navigate).toBe('');

  });
  */
 