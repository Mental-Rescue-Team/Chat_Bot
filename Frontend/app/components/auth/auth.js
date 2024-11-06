/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */


import React, { useState } from 'react';
import { Text, View, TouchableWithoutFeedback, Button } from 'react-native';
import { styles } from '../../styles/style';
import { TextInput } from 'react-native-paper';
import { connect } from 'react-redux';
import { signIn, signUp } from '../../store/actions/user_actions';
import { bindActionCreators } from 'redux';
import ValidationRules from '../../utils/forms/validationRules';
import { setTokens } from '../../utils/misc';

const AuthSample = ({goWithoutLogin}) =>  {
    const [type, setType] = useState('Login');
    const [action, setAction] = useState('로그인');
    const [actionMode, setActionMode] = useState('회원가입');
    const [hasErrors, setHasErrors] = useState(false);
    const [form, setForm] = useState({
        email: {
            value: "",
            type: 'textinput',
            rules: {
                isRequired: true,
                isEmail: true
            },
            valid: false
        },
        password: {
            value: "",
            type: 'textinput',
            rules: {
                isRequired: true,
                minLength: 6
            },
            valid: false
        },
        confirmPassword: {
            value: "",
            type: 'textinput',
            rules: {
                confirmPassword: 'password'
            },
            valid: false
        }
    });
    
    updateInput = (name, value) => {
        setHasErrors(false);
        let formCopy = form;
        formCopy[name].value = value;

        let rules = formCopy[name].rules;
        let valid = ValidationRules(value, rules, formCopy);
        formCopy[name].valid = valid;

        setForm(form => {
            return {...formCopy};
        });
        // console.warn(form);
    }

    confirmPassword = () => {
        return type != 'Login' ? (
            <TextInput 
                value={form.confirmPassword.value}
                type={form.confirmPassword.type}
                onChangeText={value => updateInput('confirmPassword', value)}
                secureTextEntry
                placeholder="ConfirmPassword"
                mode='outlined'
            />
        ) : null;
    }

    formHasErrors = () => {
        return hasErrors ? (
            <View>
                <Text>
                    로그인 정보를 다시 확인해주세요
                </Text>
            </View>
        ) : null;
    }

    submitUser = () => {
        //Init.
        let isFormValid = true;
        let submittedForm = {};
        const formCopy = form;
    
        for (let key in formCopy) {
          if (type === 'Login') {
            if (key !== 'confirmPassword') {
              isFormValid = isFormValid && formCopy[key].valid;
              submittedForm[key] = formCopy[key].value;
            }
          } else {
            isFormValid = isFormValid && formCopy[key].valid;
            submittedForm[key] = formCopy[key].value;
          }
        }
    
        if (isFormValid) {
          if (type === 'Login') {
            // console.log('로그인: ');
            // for (let key in submittedForm) {
            //   console.log(submittedForm[key]);
            // }
            
            signIn(submittedForm).then(() => {
                manageAccess();
            })

            // dispatch(signIn(submittedForm)).then((response) => {
            //     manageAccess(response);
            // }).catch((error) => {
            //     console.error("Login error:", error);
            //     setHasErrors(true);
            // });

          } else {
            // console.log('회원가입: ');
            // for (let key in submittedForm) {
            //   console.log(submittedForm[key]);
            // }
            
            signUp(submittedForm).then(() => {
                manageAccess();
            });
          }
        } else {
          setHasErrors(true);
        }
    };

    manageAccess = () => {
        if(props.User.auth.userId) {
            // this.setState({hasErrors: true})
            setHasErrors(true);
        } else {
            setTokens(props.User.auth, () =>{
                // this.setState({hasErrors: false});
                setHasErrors(false);
                props.goWithoutLogin();
            })
        }
    }

    changeForm = () => {
        type === 'Login'
          ? (setType('Sign Up'), setAction('회원가입'), setActionMode('로그인 화면으로'))
          : (setType('Login'), setAction('로그인'), setActionMode('회원가입'));
    };

    return (
        <View style = {styles.container}>
            <Text style={styles.mainText}>{type}</Text> 
            <View style={styles.mainView}>
                <TextInput style={styles.textInput}
                    value={form.email.value}
                    type={form.email.type}
                    onChangeText={value => updateInput('email', value)}
                    placeholder="E-mail"
                    mode='outlined'
                />
                <TextInput style={styles.textInput}
                    value={form.password.value}
                    type={form.password.type}
                    onChangeText={value => updateInput('password', value)}
                    secureTextEntry
                    placeholder="Password"
                    mode='outlined'
                />
                {confirmPassword()}
                {formHasErrors()}
            </View>
            <TouchableWithoutFeedback>
                <View style={{marginBottom: 20}}>
                    {/* <Text style={styles.buttext}>로그인</Text> */}
                    <Button title={action} color='#7A5ADB' onPress={submitUser}/>
                </View>
            </TouchableWithoutFeedback>
            <TouchableWithoutFeedback>
                <View style={{marginBottom: 20}}>
                    {/* <Text style={styles.text}>계정이 없으신가요? 회원가입</Text> */}
                    <Button title={actionMode} color='#7A5ADB' onPress={changeForm}/>
                </View>
            </TouchableWithoutFeedback>
            <TouchableWithoutFeedback>
                <View style={{marginBottom: 20}}>
                    {/* <Text style={styles.text}>계정이 없으신가요? 회원가입</Text> */}
                    <Button title="비회원 로그인" color='#7A5ADB' onPress={() => goWithoutLogin()}/>
                </View>
            </TouchableWithoutFeedback>
        </View>
    )
};

function mapStateToProps(state) {
    return {
        User: state.User
    }
}

function mapDispatchToProps(dispatch) {
    return bindActionCreators({signIn, signUp}, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(AuthSample);
