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
import { expect, jest } from '@jest/globals';
import { Session } from '../../interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Router, Routes } from '@angular/router';
import { of } from 'rxjs';
import { any } from 'cypress/types/bluebird';












describe ('FormComponent : tests spécifiques de SUBMIT et de EXIT PAGE : admin=true et update=false', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let value = {name:"guin",date:new Date("2026-0ComponentFixture<FormComponent>2-28"),teacher_id:1,description:"pilate"};

  const sessionServiceMock = {
    sessionInformation: {
      admin: true
    }
  }
  const sessionApiMock = {
    create: jest.fn().mockReturnValue(of({} as Session)),
    update: jest.fn().mockReturnValue(of({} as Session)),
  };
  const snackBarMock = {
    open: jest.fn()
  };

  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes([  { path: 'sessions', component: ComponentFixture<FormComponent> }  ]),
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
        { provide: SessionService, useValue: sessionServiceMock }, //admin=true
        { provide: SessionApiService, useValue: sessionApiMock }, 
        { provide: MatSnackBar, useValue: snackBarMock }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent); //pbm
    component = fixture.componentInstance;
    fixture.detectChanges();   // ngOnInit()

  });














  it ('should call create when onUpdate is false', () =>{
    component.onUpdate=false;
    component.sessionForm?.setValue(value);
    component.submit();

    expect(sessionApiMock.create).toHaveBeenCalledWith(
      component.sessionForm?.value as Session
    );

    expect(snackBarMock.open).toHaveBeenCalledWith(
      'Session created !','Close',{ duration: 3000 } 
    );

  });

});







describe ('FormComponent : tests spécifiques de SUBMIT et de EXIT PAGE : admin=true et update=true', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let theId: string;
  let value = {name:"guin",date:new Date("2026-02-28"),teacher_id:1,description:"pilate"};

  const sessionServiceMock = {
    sessionInformation: {
      admin: true
    }
  }

  const sessionApiMock = {
    create: jest.fn().mockReturnValue(of({} as Session)),
    update: jest.fn().mockReturnValue(of({} as Session)),
  };
  const snackBarMock = {
    open: jest.fn()
  };

  beforeEach(async () => {
    
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule.withRoutes([  { path: 'sessions', component: ComponentFixture<FormComponent> }  ]),
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
        { provide: SessionService, useValue: sessionServiceMock }, //admin=false
        { provide: SessionApiService, useValue: sessionApiMock }, 
        { provide: MatSnackBar, useValue: snackBarMock }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    theId = "\"1\"";
    fixture = TestBed.createComponent(FormComponent); //pbm
    component = fixture.componentInstance;
    fixture.detectChanges();   // ngOnInit()

  });

  it ('should call update when onUpdate is true', () =>{
    component.onUpdate=true;
    component.setId(theId);
    component.sessionForm?.setValue(value);
    component.submit();

    expect(sessionApiMock.update).toHaveBeenCalledWith(
      theId, component.sessionForm?.value as Session
    );

    expect(snackBarMock.open).toHaveBeenCalledWith(
      'Session updated !','Close',{ duration: 3000 } 
    );

  });

});




describe('FormComponent.ngOnInit with mock and admin=true', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let router: Router;
  
  const routes: Routes = [
    { path: 'sessions/:id', component: ComponentFixture<FormComponent> },
    { path: 'sessions/update/:id', component: ComponentFixture<FormComponent> },
    { path: 'sessions', component: ComponentFixture<FormComponent> }
  ];

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
      declarations: [FormComponent]
    }).compileComponents();

    router = TestBed.inject(Router);
    router.navigateByUrl('/sessions/1&update=true');  // l’URL que le composant verra
    router.initialNavigation();         // attend que la navigation soit terminée
    
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();   // ngOnInit() lit l’URL via ActivatedRoute
  });

  it('should set update to true', () => {
    component.ngOnInit();
    expect(component.onUpdate).toBeTruthy();
  });

});






describe('FormComponent.form completion', () => {
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


});










