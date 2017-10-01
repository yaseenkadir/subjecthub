import { Component, OnInit } from '@angular/core';
import {SubjectCommentsService} from "../services/subject-comments.service";
import {SubjectComment} from "../models/subject-comment";
import {forEachComment} from "tslint";
import {forEach} from "@angular/router/src/utils/collection";

@Component({
  selector: 'app-subject-comments',
  templateUrl: './subject-comments.component.html',
  styleUrls: ['./subject-comments.component.css'],
  providers: [SubjectCommentsService]
})

export class SubjectCommentsComponent implements OnInit {

  comments: SubjectComment[];
  fatalMessage: string = null;
  sysErrorMessageTemplate: string = "Comments system has encountered an error, please refresh.";
  message: string = null;
  canAddComment: boolean = false;
  universityId: number = 1; //to be provided by parent in the future
  subjectId: number = 1; //to be provided by parent in the future

  constructor(private subjectCommentsService: SubjectCommentsService) {

  }

  ngOnInit() {
      this.fetch();
  }

  fetch(){
      this.fatalMessage = null;
      this.canAddComment = false;
      this.subjectCommentsService.fetch(this.universityId, this.subjectId)
          .then(result => {
              if(result.isSuccessful()){
                  this.comments = result.response;
              }else{
                  this.fatalMessage = result.response['message'];
              }
              if(this.isAuth()) {
                  this.canAddComment = true;
              }
          })
          .catch(e => {
              this.fatalMessage = this.sysErrorMessageTemplate;
              console.log(e)
              this.canAddComment = false;
          });
  }

  isAuth(){
      //stub for auth check, not sure how
      this.canAddComment = true;
  }

  thumbUp(commentId: number){
      this.message = null;
      this.subjectCommentsService.addThumbUp(this.universityId, this.subjectId, commentId)
          .then(result => {
              if(result.isSuccessful()){
                  this.placeComment(result.response);
              }else{
                  this.message = result.response['message'];
              }
          })
          .catch(e => {
              this.message = this.sysErrorMessageTemplate;
              console.log(e)
          });
  }

    thumbDown(commentId: number){
        this.message = null;
        this.subjectCommentsService.addThumbDown(this.universityId, this.subjectId, commentId)
            .then(result => {
                if(result.isSuccessful()){
                    this.placeComment(result.response)
                }else{
                    this.message = result.response['message'];
                }
            })
            .catch(e => {
                this.message = this.sysErrorMessageTemplate;
            });
    }

    flag(commentId: number){
        this.message = null;
        this.subjectCommentsService.flag(this.universityId, this.subjectId, commentId)
            .then(result => {
                if(result.isSuccessful()){
                    this.placeComment(result.response)
                }else{
                    this.message = result.response['message'];
                }
            })
            .catch(e => {
                this.message = this.sysErrorMessageTemplate;
            });
    }

    unflag(commentId: number){
        this.message = null;
        this.subjectCommentsService.unFlag(this.universityId, this.subjectId, commentId)
            .then(result => {
                if(result.isSuccessful()){
                    this.placeComment(result.response)
                }else{
                    this.message = result.response['message'];
                }
            })
            .catch(e => {
                this.message = this.sysErrorMessageTemplate;
            });
    }

    addComment(message: string){
        this.message = null;
        //need auth? or is spring enough
        if(this.canAddComment){
            this.subjectCommentsService.add(this.universityId, this.subjectId, message)
                .then(result => {
                    if(result.isSuccessful()){
                        this.placeComment(result.response)
                    }else{
                        this.message = result.response['message'];
                    }
                })
                .catch(e => {
                    this.message = this.sysErrorMessageTemplate.replace("."," ") + "or login.";
                });
        }
    }

    placeComment(response: SubjectComment){
        let found : boolean = false;
        this.comments.forEach(currentValue => {
            if(currentValue.id = response.id){
                currentValue = response;
                found = true;
            }
        });
        if (!found){
            this.comments.push(response);
        }
    }

}
