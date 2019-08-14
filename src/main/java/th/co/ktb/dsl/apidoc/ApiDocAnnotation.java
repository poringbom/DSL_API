package th.co.ktb.dsl.apidoc;

import java.lang.annotation.Annotation;

import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;

public class ApiDocAnnotation {
	public static ApiParam getInstanceOfApiParam(final ParamName field)
    {
		ApiParam annotation = new ApiParam()
        {
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return ApiParam.class;
            }

			@Override
			public String name() {
				if      (field == ParamName.LOAN_TYPE) return "Loan type";
				else if (field == ParamName.ACCT_NO) return "Loan account number";
				else if (field == ParamName.POSTPONE_RQ_NUMBER) return "Postpone request number";
				else if (field == ParamName.SUSPEND_RQ_NUMBER) return "Postpone request number";
				else if (field == ParamName.OFFSET) return "Data offset";
				else if (field == ParamName.SIZE) return "Limit data size (-1 = unlimit)";
				else return "";
			}

			@Override
			public String value() {
				if      (field == ParamName.LOAN_TYPE) return "Loan type";
				else if (field == ParamName.ACCT_NO) return "Loan account number";
				else if (field == ParamName.POSTPONE_RQ_NUMBER) return "Postpone request number";
				else if (field == ParamName.SUSPEND_RQ_NUMBER) return "Suspend request number";
				else if (field == ParamName.OFFSET) return "Data offset";
				else if (field == ParamName.SIZE) return "Limit data size (-1 = unlimit)";
				else return "";
			}

			@Override
			public String defaultValue() {
				if (field == ParamName.OFFSET) return "1";
				else if (field == ParamName.SIZE) return "-1";
				else return "";
			}

			@Override
			public String allowableValues() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean required() {
				if      (field == ParamName.LOAN_TYPE) return true;
				else if (field == ParamName.ACCT_NO) return true;
				else if (field == ParamName.POSTPONE_RQ_NUMBER) return true;
				else if (field == ParamName.SUSPEND_RQ_NUMBER) return true;
				else if (field == ParamName.OFFSET) return false;
				else if (field == ParamName.SIZE) return false;
				else return false;
			}

			@Override
			public String access() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean allowMultiple() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hidden() {
				return false;
			}

			@Override
			public String example() {
				if (field == ParamName.OFFSET) return "1";
				else if (field == ParamName.SIZE) return "-1";
				else return "";
//				return null;
			}

			@Override
			public Example examples() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String type() {
				if      (field == ParamName.LOAN_TYPE) return "path";
				else if (field == ParamName.ACCT_NO) return "path";
				else if (field == ParamName.POSTPONE_RQ_NUMBER) return "path";
				else if (field == ParamName.SUSPEND_RQ_NUMBER) return "path";
				else if (field == ParamName.OFFSET) return "query";
				else if (field == ParamName.SIZE) return "query";
				else return null;
			}

			@Override
			public String format() {
				return "";
			}

			@Override
			public boolean allowEmptyValue() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean readOnly() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String collectionFormat() {
				// TODO Auto-generated method stub
				return null;
			}
        };

        return annotation;
    }
}

enum ParamName {
	LOAN_TYPE,
	ACCT_NO,
	OFFSET,
	SIZE,
	POSTPONE_RQ_NUMBER,
	SUSPEND_RQ_NUMBER
}
