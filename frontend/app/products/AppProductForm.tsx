import React, {useEffect, useState} from "react";
import {Product} from "@/app/products/page";
import AppFormInput from "@/app/components/AppFormInput";
import {AppFieldValidators, AppFieldValidatorsError, AppValidate} from "@/app/data/Validators";
import AppDataViewItem from "@/app/components/AppDataViewItem";
import AppDataView from "@/app/components/AppDataView";
import AppActionMenu from "@/app/components/AppActionMenu";
import AppButton from "@/app/components/AppButton";
import Image from "next/image";
import AppFormSelect from "@/app/components/AppFormSelect";
import {allCountriesToCode} from "@/app/data/CountriesRepository";


export interface ProductFormData {
    name: string;
    description: string;
    brand: string;
    manufacturer: string;
    supplier: string;
    category: string;
    subCategory: string;
    originCountryCode: string;
}

const FormDataValidator: AppFieldValidators = {
    name: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    description: {
        required: true,
        min: 5,
        max: 254,
        format: "alphanumeric"
    },
    brand: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    manufacturer: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    supplier: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    category: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    subCategory: {
        required: true,
        min: 5,
        max: 50,
        format: "alphanumeric"
    },
    originCountryCode: {
        required: true,
        min: 2,
        max: 2,
        format: "text"
    },
}

interface Props {
    state: "NEW" | "EDIT";
    data: Product | null;
    loading: boolean;
    onSave: (data: ProductFormData) => void;
    onCancel: () => void;
}

const AppProductForm: React.FC<Props> = (props: Props) => {

    const {id, code, status, ...dataModifiable} = props.data || {};
    const [formData, setFormData] = useState<ProductFormData>(
        {
            name: "",
            description: "",
            brand: "",
            manufacturer: "",
            supplier: "",
            category: "",
            subCategory: "",
            originCountryCode: "",
            ...dataModifiable
        }
    );
    const [errors, setErrors] = useState<AppFieldValidatorsError>({});
    const [touched, setTouched] = useState<boolean>(false);

    useEffect(() => {
        if (!touched) return;
        setErrors(AppValidate(formData, FormDataValidator));
    }, [formData, touched]);

    const onSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        props.onSave(formData);
    }

    const handleInputChange = (field: string, value: any) => {
        setTouched(true);
        setFormData((prev: any) => prev ? {
            ...prev,
            [field]: value
        } : prev);
    };

    return (
        <div>
            <form onSubmit={onSubmit}>
                <AppDataView>

                    {props.data?.id && (
                        <AppDataViewItem title="Product ID">
                            {props.data.id}
                        </AppDataViewItem>
                    )}

                    {props.data?.code && (
                        <AppDataViewItem title="Product Code">
                            {props.data.code}
                        </AppDataViewItem>
                    )}

                    <AppFormInput title="Name" description="Product name" className="flex flex-col"
                                  type="text" required={true} value={formData.name}
                                  errors={errors.name}
                                  onChange={(value) => handleInputChange("name", value)}
                    />

                    <AppFormInput title="Description" description="Product details" className="flex flex-col"
                                  type="text" required={true} value={formData.description}
                                  errors={errors.description}
                                  onChange={(value) => handleInputChange("description", value)}
                    />

                    <AppFormInput title="Brand" description="Product brand" className="flex flex-col"
                                  type="text" required={true} value={formData.brand}
                                  errors={errors.brand}
                                  onChange={(value) => handleInputChange("brand", value)}
                    />

                    <AppFormInput title="Manufacturer" description="Manufacturer name" className="flex flex-col"
                                  type="text" required={true} value={formData.manufacturer}
                                  errors={errors.manufacturer}
                                  onChange={(value) => handleInputChange("manufacturer", value)}
                    />

                    <AppFormInput title="Supplier" description="Supplier name" className="flex flex-col"
                                  type="text" required={true} value={formData.supplier}
                                  errors={errors.supplier}
                                  onChange={(value) => handleInputChange("supplier", value)}
                    />

                    <AppFormInput title="Category" description="Product category" className="flex flex-col"
                                  type="text" required={true} value={formData.category}
                                  errors={errors.category}
                                  onChange={(value) => handleInputChange("category", value)}
                    />

                    <AppFormInput title="Sub Category" description="Product sub-category" className="flex flex-col"
                                  type="text" required={true} value={formData.subCategory}
                                  errors={errors.subCategory}
                                  onChange={(value) => handleInputChange("subCategory", value)}
                    />

                    <AppFormSelect title="Made In" description="Country of origin" className="flex flex-col"
                                   errors={errors.originCountryCode}
                                   itemSelected={formData.originCountryCode} items={allCountriesToCode}
                                   onChange={(value) => handleInputChange("originCountryCode", value)}
                    />

                </AppDataView>
                <AppActionMenu>
                    <AppButton variant="secondary" onClick={props.onCancel}>
                        Cancel
                    </AppButton>
                    <AppButton variant="primary" type="submit" disabled={props.loading}>
                        <Image src="/save.svg" alt="View" width={24} height={24}/>
                        {props.loading ? "Saving..." : "Save"}
                    </AppButton>
                </AppActionMenu>
            </form>
        </div>
    )
}

export default AppProductForm;
