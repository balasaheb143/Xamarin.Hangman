using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace BaseGameUtils
{
    public class SignInFailureReason 
    {
        public int NO_ACTIVITY_RESULT_CODE = -100;
        int mServiceErrorCode = 0;
        public int mActivityResultCode = NO_ACTIVITY_RESULT_CODE;

        public int getServiceErrorCode() {
            return mServiceErrorCode;
        }

        public int getActivityResultCode() {
            return mActivityResultCode;
        }

        public SignInFailureReason(int serviceErrorCode, int activityResultCode) 
        {
            mServiceErrorCode = serviceErrorCode;
            mActivityResultCode = activityResultCode;
        }

        public SignInFailureReason(int serviceErrorCode) : base(serviceErrorCode, NO_ACTIVITY_RESULT_CODE)
        {
        }

        public override String ToString() 
        {
            return "SignInFailureReason(serviceErrorCode:"
                    + GameHelperUtils.errorCodeToString(mServiceErrorCode)
                    + ((mActivityResultCode == NO_ACTIVITY_RESULT_CODE) ? ")"
                    : (",activityResultCode:"
                    + GameHelperUtils.activityResponseCodeToString(mActivityResultCode) + ")"));
        }
    }
}